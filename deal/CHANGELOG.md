# Change log
## 1.0.0
* MVP-2 microservice 'Deal'
## 1.1.0
* Added kafka producer for messaging with e-mail sending consumer
* Added embedded testing and fixed old ones which had problems with liquibase
* Moved and changed feign exception handler from config to controller advice
* Added endpoints for document creation and statement administration
* Removed unused exceptions
* Fixed wrong status code on Precondition failed
* Updated logging for neostudy-lib usage
* Updated tests for Mockito.verify usage
* Add kubernetes + skaffold support
### 1.1.1
* Updating logger-utils with lib-starter