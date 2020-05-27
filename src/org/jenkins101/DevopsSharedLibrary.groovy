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
        def appName    = context.steps.sh(script: "mvn -q -N org.codehaus.mojo:exec-maven-plugin:1.3.1:exec -Dexec.executable='echo' -Dexec.args='\${project.artifactId}'", returnStdout: true).trim()
        def appVersion = context.steps.sh(script: "mvn -q -N org.codehaus.mojo:exec-maven-plugin:1.3.1:exec -Dexec.executable='echo' -Dexec.args='\${project.version}'", returnStdout: true).trim()
        def jarPath    = "target/${appName}-${appVersion}.jar"
        
        context.steps.sh "docker build --no-cache --build-arg JAR_FILE_PATH=${jarPath} -t demo/${appName}:${appVersion} ."

        //Simulacion de despliegue
        
        context.steps.sh "docker stop ${appName}"
        context.steps.sh "docker rm ${appName}"
        context.steps.sh "docker run --name ${appName} -d -p 9966:9966 demo/${appName}:${appVersion}"
    }
}