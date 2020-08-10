package com.sundeinfo.core.permission.strategy;


import com.sundeinfo.core.permission.service.*;
import com.sundeinfo.core.permission.service.impl.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

public class SecurityServiceConfigurationSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private AnyUserService anyUserService;

    private GeneralMetadataSourceService generalMetadataSourceService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setServices(){
        anyUserService = (AnyUserService)applicationContext.getBean("anyUserService");
        anyUserService.setServices(new DefaultUserService());
        generalMetadataSourceService = (GeneralMetadataSourceService)applicationContext.getBean("generalMetadataSourceService");
        generalMetadataSourceService.setServices(new DefaultPermissionService());
    }

    public void setServices(UserDomainService userService){
        anyUserService = (AnyUserService)applicationContext.getBean("anyUserService");
        anyUserService.setServices(Optional.ofNullable(userService).orElse(new DefaultUserService()));
        generalMetadataSourceService = (GeneralMetadataSourceService)applicationContext.getBean("generalMetadataSourceService");
    }

}
