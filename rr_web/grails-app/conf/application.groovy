// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'mx.uaemex.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'mx.uaemex.UserRole'
grails.plugin.springsecurity.authority.className = 'mx.uaemex.Role'
grails.plugin.springsecurity.rejectIfNoRule = true
grails.plugin.springsecurity.fii.rejectPublicInvocations = true
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/login/**',       access: ['permitAll']],
  [pattern: '/logout/**',      access: ['permitAll']],
	[pattern: '/user/**',        access: 'permitAll'],
	[pattern: '/bitacora/**',        access: 'IS_AUTHENTICATED_FULLY'],
	[pattern: '/career/**',        access: 'ROLE_ADMIN'],
	[pattern: '/laboratory/**',        access: 'ROLE_ADMIN'],
	[pattern: '/resource/**',        access: 'ROLE_ADMIN'],
	[pattern: '/teacher/**',        access: 'ROLE_ADMIN'],
	[pattern: '/document/**',        access: 'ROLE_USER'],
	[pattern: '/documents/**',        access: 'ROLE_USER']
]

