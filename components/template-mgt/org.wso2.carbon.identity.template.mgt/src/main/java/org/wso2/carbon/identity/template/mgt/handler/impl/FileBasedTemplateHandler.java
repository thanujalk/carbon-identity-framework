/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.template.mgt.handler.impl;

import org.wso2.carbon.identity.template.mgt.exception.TemplateManagementException;
import org.wso2.carbon.identity.template.mgt.handler.ReadOnlyTemplateHandler;
import org.wso2.carbon.identity.template.mgt.model.Template;
import org.wso2.carbon.identity.template.mgt.model.TemplateInfo;

import java.util.List;

/**
 * File based template handler implementation.
 */
public class FileBasedTemplateHandler implements ReadOnlyTemplateHandler {

    @Override
    public Template getTemplateById(String templateId) throws TemplateManagementException {

        return null;
    }

    @Override
    public List<TemplateInfo> listTemplates(Integer limit, Integer offset) throws TemplateManagementException {

        return null;
    }

    @Override
    public List<Template> listTemplates(String templateType, Integer limit, Integer offset)
            throws TemplateManagementException {

        return null;
    }
}
