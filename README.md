# Hometask #17
### _@Hibernate_
1. Git repo
2. README file with rules from lecture
3. create new maven/gradle project
4. Create 2 POJO classes with 1 to 1 relation
5. Create DB schema for classes from #4
6. Add JPA + hibernate libs to project
7. Configure POJO mapping with JPA annotations
8. Create CRUD DAO (use EntityManager) for POJOs
9. Tests
### Start script
> docker-compose up -d 
#### start migrations:
> mvn flyway:migrate

### Standard links
+ [Pom xml](pom.xml)
+ [Main class](src/main/java/by/itacademy/javaenterprise/goralchuk/App.java)
+ [Migration path](src/main/resources/database/migration)
+ [Database properties](src/main/resources/database/database.properties)

### Some questions:
1. The script (docker-compose) below is not working in docker compose...no rights to share the resource...maybe 
   Windows is 
   interfering
> flyway:
image: flyway/flyway
command: -url=jdbc:mariadb://127.0.0.1:3306/hibernateTest?useUnicode=true&characterEncoding=UTF-8 -schemas=hibernateTest -user=user -password=userpass -connectRetries=60 migrate
volumes: - ../src/main/resources/database/migration:flyway/sql
depends_on: - db
2. The Manifest.mf is not working / is not update in directory "target" after assembling the project and jar file. 
   In pom.xml is not working either
+ [MANIFEST.FM](src/main/resources/META-INF/MANIFEST.MF)
+ [Pom xml](pom.xml)
3. How to use one database file for all properties?
+ [Database properties](src/main/resources/database/database.properties)
+ [persistence.xml](src/main/resources/META-INF/persistence.xml)
