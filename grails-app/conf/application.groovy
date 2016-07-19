grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/**',      access: ['permitAll']]
]
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.webNo = '01'
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.ybg.rp.ua.partner.PartnerUserInfo'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.ybg.rp.ua.partner.PartnerUserAuthority'
grails.plugin.springsecurity.auth.loginFormUrl = '/login/form'
grails.plugin.springsecurity.authority.className = 'com.ybg.rp.ua.partner.PartnerAuthorityInfo'
grails.plugin.springsecurity.authority.nameField = 'authority'
grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/500'
grails.plugin.springsecurity.failureHandler.ajaxAuthFailUrl = '/500'
grails.plugin.springsecurity.adh.errorPage = '/403'
grails.plugin.springsecurity.adh.ajaxErrorPage = '/403'