package edu.stanford.slac.ad.eed.baselib;


import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    @Autowired
    AppProperties appProperties;
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

