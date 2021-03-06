/*
 * Copyright 2015 floragunn UG (haftungsbeschränkt)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.floragunn.searchguard.http;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchSecurityException;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.util.concurrent.ThreadContext;
import org.elasticsearch.http.netty4.Netty4HttpRequest;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.threadpool.ThreadPool;

import com.floragunn.searchguard.configuration.ConfigurationChangeListener;
import com.floragunn.searchguard.support.ConfigConstants;

public class XFFResolver implements ConfigurationChangeListener {

    protected final Logger log = LogManager.getLogger(this.getClass());
    private volatile boolean enabled;
    private volatile RemoteIpDetector detector;
    private final ThreadContext threadContext;
        
    public XFFResolver(final ThreadPool threadPool) {
        super();
        this.threadContext = threadPool.getThreadContext();
    }

    public TransportAddress resolve(final RestRequest request) throws ElasticsearchSecurityException {
        
        if(log.isTraceEnabled()) {
            log.trace("resolve {}", request.getRemoteAddress());
        }
        
        if(enabled && request.getRemoteAddress() instanceof InetSocketAddress && request instanceof Netty4HttpRequest) {

            final InetSocketAddress isa = new InetSocketAddress(detector.detect((Netty4HttpRequest) request, threadContext), ((InetSocketAddress)request.getRemoteAddress()).getPort());
        
            if(isa.isUnresolved()) {           
                throw new ElasticsearchSecurityException("Cannot resolve address "+isa.getHostString());
            }
                
             
            if(log.isTraceEnabled()) {
                if(threadContext.getTransient(ConfigConstants.SG_XFF_DONE) == Boolean.TRUE) {
                    log.trace("xff resolved {} to {}", request.getRemoteAddress(), isa);
                } else {
                    log.trace("no xff done for {}",request.getClass());
                }
            }
            return new InetSocketTransportAddress(isa);
        } else if(request.getRemoteAddress() instanceof InetSocketAddress){
            
            if(log.isTraceEnabled()) {
                log.trace("no xff done (enabled or no netty request) {},{},{},{}",enabled, request.getClass());

            }
            return new InetSocketTransportAddress((InetSocketAddress)request.getRemoteAddress());
        } else {
            throw new ElasticsearchSecurityException("Cannot handle this request. Remote address is "+request.getRemoteAddress()+" with request class "+request.getClass());
        }
    }

    @Override
    public void onChange(final Settings settings) {
        enabled = settings.getAsBoolean("searchguard.dynamic.http.xff.enabled", true);
        if(enabled) {
            detector = new RemoteIpDetector();
            detector.setInternalProxies(settings.get("searchguard.dynamic.http.xff.internalProxies", detector.getInternalProxies()));
            detector.setProxiesHeader(settings.get("searchguard.dynamic.http.xff.proxiesHeader", detector.getProxiesHeader()));
            detector.setRemoteIpHeader(settings.get("searchguard.dynamic.http.xff.remoteIpHeader", detector.getRemoteIpHeader()));
            detector.setTrustedProxies(settings.get("searchguard.dynamic.http.xff.trustedProxies", detector.getTrustedProxies()));
        } else {
            detector = null;
        }
    }
}
