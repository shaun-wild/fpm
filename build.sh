native-image --no-fallback -cp fpm-1.0-SNAPSHOT.jar -H:Name=fpm -H:Class=com.qardz.fpm.ApplicationKt -H:ReflectionConfigurationFiles=../../reflect-config.json -H:ResourceConfigurationFiles=../../resource-config.json