# itdoes
## 1.	Introduction
    Itdoes is a web development framework which can be easily extended to add/configure new features for common web software systems. User can focus only on their own business logics based on this framework.  
    It provides common features including database access, transaction management, cache control, authentication/authorization, full text search, security, Restful services, file upload, email, ftp, Web socket, XMPP, code generator, and many other useful utilities like reflections, bean populators, object pool.  
    It encapsulates may popular frameworks/libs, like Spring, Hibernate, Shiro, SpringMvc, Spring-Data-Jpa, Validator, MySQL, Slf4j/logback, Jackson, JAXB, Cglib, Dozer, FreeMarker, JPA, Digest, Crypto, POI, Seleinum. It also provides examples for unit (Mockito, PowerMock), integeration, functional test (Spring-test, Selenium).  
    Following is just notes for each layer while designing the framework. It would be refined in the future to add user-friendly documentations.  

## 2.	Web
### 2.1	SpringMvc & Restful
- SpringMvc is a single framework which is easy to implement Restful comparing Struts2 and other Web frameworks.  
-	Separate definition of Service and Web. Web definition is a child of service one, which means it can get all information from Service, but Service cannot be the information form Web. Service will scan all @Component expect @Controller and @ControllerAdvice.  
-	Use @ModelAttribute for post and put which needs populate POs at runtime.  
-	Use @ExceptionHandler to handle all the exceptions thrown in any space. Other exceptions handlers are defined either in a SimpleMappingExceptionResolver and web.xml.  
-	Use Validation framework to validate the POs when post and put. This is a check for “hack” only since client UI has already checked that.  
-	For Date<->String convert, register a @InitBinder in BaseController to only accept number instead of date display format to avoid timezone issue.   
-	Use @RestController to return all data in Json format instead of render a JSP.  
-	Use a Forcade Controller to handle all the simple table-entity data.  
-	The real Restful is not defined in HTML5 and can only be supported in HTTP which means only APIs using XmlHttpRequest can be acted as a Restful client. The normal Html form cannot be used. As a common framework, finally use a pattern which is: <Resoucce>/<Operation>/[ID].
### 2.2	Media Type
o	Use Json/html/text/xml withUTF8 to support multi-byte languages.
## 3.	Service
### 3.1	Spring
o	Use profile to separate different environment, such as: production, development, unit-test, function-test.
o	Use a local properties file to control local environment.
o	Set default profile to production and active the other profile at runtime to override production.
o	Use @Autowired instead of @Resource to inject beans.
### 3.2	Shiro
o	Use Shiro to do AA(Authentication and Authorization) work.
o	Authentication is normal and use Shiro login filter.
o	Shiro is enhanced to support adding more access control URLs.
o	Enhance Shiro to support Ajax calls.
### 3.3	Security
o	Separate authorization into two categories: access control and data control.
o	Separate authorization into three phases: definition, assignment, match
o	Access control definition is done automatically by scanning entities at server starting phase.
o	Access control assignment is done whenever a use is logging
o	Access control match is done when a user accesses a secure URL by comparing it with assignment.
o	Data control definition is done by adding a new annotation @SecureColumn in entities.
o	Data control assignment is done when a user is logging in.
o	Data control match is done anytime a user accesses an entity with secure columns, such as: search/get/post/put. 
### 3.4	Digest
o	Sha256 is used for authentication.
### 3.5	Crypto
o	AES may be used as data encryption/decryption.
### 3.6	Transaction
o	Transaction is defined by @Transactional in service layer.
o	Default is read-only which is defined in Base service.
o	Any modification operation like post, put, and delete should be set to read-only = false.
### 3.7	Cache
o	Some business caches can be set in service layer apart from the second-level cache in Hibernate.
o	Ehcache is a standard one to use.
o	Guava cache s good to use for its simple strategy definition in API.
### 3.8	Email
o	Email util can be used to send email with plain text or html with attachment.
## 4.	Repository
### 4.1	Spring Data JPA
o	All repositories are inherited from Page and Specification support repository.
o	Each entity must have its own repository which will be loaded at runtime automatically by EntityService to enable façade access and security check.
### 4.2	Hibernate
o	Hibernate is used as the ORM.
o	Now, no relationship is defined (in business level instead of framework perspective) but framework itself support relationship to CRUD.
### 4.3	MySQL
o	MySQL is used as the default production database.
o	Framework itself is not restricted to use any databases and they can be defined in configuration file.
o	MySQL Jdbc Url must be defined to support reconnection for database connection pool
### 4.4	H2
o	H2 is used as a test database.
o	It can be defined in Jdbc Url as a memory one or a file.
o	No table needs to be created manually.
### 4.5	Database Connection Pool
o	Tomcat Jdbc is used because of its perfect performance.
### 4.6	Cache
o	Ehcache is used as the Hibernate secondary cache.
o	Default cache is defined. Each entity can define its own cache without using default.
## 5.	Entity
### 5.1	JPA
o	All entities are standard JPA entities.
o	Entities inherit from BaseEntity.
### 5.2	Cache
o	Cache is defined using Hibernate @cache in each entity which needs to be cached.
## 6.	Common
### 6.1	Slf4j and Logback
o	Slf4j is used as the standard log API.
o	Logback is used as the real log implementation.
o	JCL, JUL, Log4j are deprecated by framework and auto dispatched to Slf4j.
### 6.2	Jackson
o	Jackson is used to do Java<->Json convert.
### 6.3	JAXB
o	JAXB is used to do Java<->Xml convert.
### 6.4	Dozer
o	Dozer is used to do Java<->Java convert.
### 6.5	FreeMarker
o	FreeMarker is used as a template for any complex data formatting, e.g. html page, html email.
### 6.6	JPA Specification
o	A util is used to automatically generate JPA specification.
### 6.7	Digest
o	Sha256, Sha1, MD5 is now supported.
### 6.8	Crypto
o	AES is supported.
o	HmacSha256, HmacSha1, HmacMD5 is supported.
### 6.9	Shiro
o	HasAnyPermission tag is supported.
### 6.10	Web
o	Common Http header processing is integrated, e.g. checkModifiedSince, checkNonMatch, setExpire, setCache.
### 6.11	String
o	Apache commons-lang3 is used.
### 6.12	IO
o	Apache commons-io is used.
### 6.13	Encode/Decode
o	Apache commons-codec is used.
o	Codecs is implemented to support Hex/Base64/Base62/URL/XML/HTML encode/decode.
### 6.14	Bean
o	Apache commons-beanutils is used.
### 6.15	Collection
o	Collections3 is implemented for collection operations.
### 6.16	Exception
o	Exceptions is implemented for common exception processing.
### 6.17	Id generation
o	Ids is implemented for UUID, random long, and random String.
### 6.18	Properties Loader
o	PropertiesLoader is implemented for loading and fetching properties from different classpaths.
### 6.19	Reflection
o	Reflections is implemented for private, final field and method set/get.
### 6.20	Thread
o	Threads is implemented for common thread management, e.g. shutdown, shutdownNow.
## 7.	Test
### 7.1	Unit Test
o	Junit4 is used.
o	Category can be used.
o	Annotation is used.
o	Rule and ClassRule can be set.
### 7.2	Database Test
o	Can use Spring test for transactional.
### 7.3	Functional Test
o	Web functional test is supported by using Selenium.
o	A runtime server, e.g. Jetty, should be started as needed.
### 7.4	Spring Test
o	Spring Application Context can be used to auto inject beans.
o	Transactional test can be tested, e.g. database.
### 7.5	Mockito
o	It can be used to separate the implementation, especially for service layer test. Database test should not use it.
### 7.6	Powermock
o	Conbined with Mockito for new, final operations.
### 7.7	Assertj
o	Fluent APIs can be used in Junit.
## 8.	Environment
### 8.1	Maven
o	Maven is used as build framework.
o	Project is separated into several modules: parent, common-jar, common-war, business-war.
o	Module parent defines all the dependencies and plugins, including the versions. But it does not really use it.
o	Module common-jar is used for common java utils.
o	Module common-war is used for a separate web context root which includes some common funtions, e.g. JS/images.
o	Module business-war is used for the real project. In future it may has other war modules like it, e.g. ERP, Finance.
### 8.2	Git
o	Git is used as code management.
### 8.3	Eclipse
o	Eclipse is used as development IDE.
o	JavaFormatter.xml (on Programming driver) should be imported for Java development.
o	JavaScriptFormatter.xml (on Programming driver) should be imported for JavaScript development.
