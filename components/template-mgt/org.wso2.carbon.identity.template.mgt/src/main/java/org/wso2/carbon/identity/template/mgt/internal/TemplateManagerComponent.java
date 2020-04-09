/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.identity.template.mgt.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.configuration.mgt.core.ConfigurationManager;
import org.wso2.carbon.identity.core.util.IdentityCoreInitializedEvent;
import org.wso2.carbon.identity.template.mgt.TemplateManager;
import org.wso2.carbon.identity.template.mgt.TemplateManagerImpl;
import org.wso2.carbon.identity.template.mgt.handler.ReadOnlyTemplateHandler;
import org.wso2.carbon.identity.template.mgt.handler.TemplateHandler;
import org.wso2.carbon.identity.template.mgt.handler.impl.ConfigStoreBasedTemplateHandler;
import org.wso2.carbon.identity.template.mgt.handler.impl.FileBasedTemplateHandler;

/**
 * OSGi declarative services component which handles registration and un-registration of template management service.
 */
@Component(
        name = "carbon.identity.template.mgt.component",
        immediate = true
)
public class TemplateManagerComponent {

    private static Log log = LogFactory.getLog(TemplateManagerComponent.class);

    /**
     * Register Template Manager as an OSGi service.
     *
     * @param componentContext OSGi service component context.
     */
    @Activate
    protected void activate(ComponentContext componentContext) {

        try {
            BundleContext bundleContext = componentContext.getBundleContext();

            bundleContext.registerService(TemplateManager.class, new TemplateManagerImpl(), null);

            //Add default template handlers
            ReadOnlyTemplateHandler fileBasedTemplateHandler =  new FileBasedTemplateHandler();
            TemplateManagerDataHolder.getInstance().addReadOnlyTemplateHandler(fileBasedTemplateHandler);
            TemplateHandler configStoreBasedTemplateHandler =  new ConfigStoreBasedTemplateHandler();
            TemplateManagerDataHolder.getInstance().addReadOnlyTemplateHandler(configStoreBasedTemplateHandler);
            if (log.isDebugEnabled()) {
                log.debug("Template Manager bundle is activated.");
            }
        } catch (Throwable e) {
            log.error("Error while activating TemplateManagerComponent.", e);
        }
    }

    @Reference(
            name = "identityCoreInitializedEventService",
            service = IdentityCoreInitializedEvent.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdentityCoreInitializedEventService"
    )
    protected void setIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started. */
    }

    protected void unsetIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started. */
    }

    @Reference(
            name = "carbon.configuration.mgt.component",
            service = ConfigurationManager.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetConfigurationManager")
    protected void setConfigurationManager(ConfigurationManager configurationManager) {

        if (log.isDebugEnabled()) {
            log.debug("Configuration Manager service is set in the Template Manager component.");
        }
        TemplateManagerDataHolder.getInstance().setConfigurationManager(configurationManager);
    }

    protected void unsetConfigurationManager(ConfigurationManager configurationManager) {

        if (log.isDebugEnabled()) {
            log.debug("Configuration Manager service is unset in the Template Manager component.");
        }
        TemplateManagerDataHolder.getInstance().setConfigurationManager(null);
    }

    @Reference(
            name = "identity.template.handler",
            service = ReadOnlyTemplateHandler.class,
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetReadOnlyTemplateHandler")
    protected void setReadOnlyTemplateHandler(ReadOnlyTemplateHandler readOnlyTemplateHandler) {

        if (log.isDebugEnabled()) {
            log.debug("Template handler " + readOnlyTemplateHandler.getClass().getName() + " is added.");
        }
        TemplateManagerDataHolder.getInstance().addReadOnlyTemplateHandler(readOnlyTemplateHandler);
    }

    protected void unsetReadOnlyTemplateHandler(ReadOnlyTemplateHandler readOnlyTemplateHandler) {

        if (log.isDebugEnabled()) {
            log.debug("Template handler " + readOnlyTemplateHandler.getClass().getName() + " is removed.");
        }
        TemplateManagerDataHolder.getInstance().removeReadOnlyTemplateHandler(readOnlyTemplateHandler);
    }
}
