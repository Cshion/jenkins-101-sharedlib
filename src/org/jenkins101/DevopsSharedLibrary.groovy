package org.jenkins101;

/**
 * Libreria compartida que contiene funciones de maven
*/
class DevopsSharedLibrary implements Serializable {
    def context;

    DevopsSharedLibrary(context){
        this.context = context;
    }

    def build(){
        sh "mvn -B -DskipTests clean package"
    }

    def unitTest(){
        try{
            context.sh "mvn test"
        }catch(e){
            throw e
        }finally{
            junit 'target/surefire-reports/TEST-*.xml'
            jacoco()
        }
    }


    def deployApplication(){
        //runMvnTask("",parameters);
    }
}