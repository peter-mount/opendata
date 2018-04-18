buildImage = 'maven:3.2-jdk-8'

def mvn = {
  cmd ->     sh 'docker run -i --rm' +
        ' -v $HOME/.m2/settings.xml:/root/.m2/settings.xml:ro' +
        ' -v "$(pwd)":/work' +
        ' -w /work' +
        ' ' + buildImage +
        ' mvn ' + cmd
}

properties([
  buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '7', numToKeepStr: '10')),
  disableConcurrentBuilds(),
  disableResume(),
  pipelineTriggers([
    cron('H H * * *')
  ])
])

node( 'Build' ) {

  stage( "prepare" ) {
    checkout scm
    sh 'docker pull ' + buildImage
  }

  [ 'clean', 'install', 'deploy' ].each {
    target -> stage( target ) {
      mvn( target)
    }
  }

}
