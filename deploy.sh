mvn clean package
asadmin undeploy referidos2intento-1.0
#asadmin deploy "/Users/carlosocando/Documentos/ionix/Desarrollando/referidos2intento/target/referidos2intento-1.0.war" && open 'http://www.localhost:4848/common/appServer/serverInstGeneralPe.jsf?instanceName=server'
asadmin deploy "$1" && open 'http://www.localhost:4848/common/appServer/serverInstGeneralPe.jsf?instanceName=server'
