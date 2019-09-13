package com.bath.tcpviz.dis;

import com.bath.tcpviz.dis.fileupload.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
		FileStorageProperties.class
})

@SpringBootApplication
public class DisApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisApplication.class, args);
	}

}
