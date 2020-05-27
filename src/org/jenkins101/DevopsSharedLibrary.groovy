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
        context.steps.sh "mvn -B -DskipTests clean package"
    }

    def unitTest(){
        try{
            context.steps.sh "mvn test"
        }catch(e){
            throw e
        }finally{
            context.steps.junit 'target/surefire-reports/TEST-*.xml'
            context.steps.jacoco()
        }
    }


    def deployApplication(){
        //runMvnTask("",parameters);
    }
}