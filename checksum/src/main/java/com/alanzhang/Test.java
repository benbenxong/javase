package com.alanzhang;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.concurrent.Callable;
@Command(name = "checksum", mixinStandardHelpOptions = true,
        version = "checksum 4.0",
        description = "Prints the checksum (MD5 by default) of a file to STDOUT.")
class Test implements Callable<Integer> {
    @Parameters(index = "0", arity = "1",
            description = "The file whose checksum to calculate.")
    private File file;
    @Option(names = {"-a", "--algorithm"},
            description = "MD5, SHA-1, SHA-256, ...")
    private String algorithm = "MD5";

    @Option(names = {"-S", "--sheet"}, split = ",", required = true)
    private String[] sheet;
    /* 本样例实现了Callable，所以解析、错误处理以及对使用帮助或版本帮助的用户请求处理
     都可以在一行代码中实现。*/
    public static void main(String... args) {
        int exitCode = new CommandLine(new Test()).execute(args);
        System.exit(exitCode);
    }
    @Override
    public Integer call() throws Exception { // the business logic...
        System.out.println("file: " + file);
        System.out.println("algorithm: " + algorithm);
        if (sheet!=null) {
            System.out.println("The commit message is");
            for (String message : sheet) {
                System.out.println(message);
            }
        }

//        byte[] data = Files.readAllBytes(file.toPath());
//        byte[] digest = MessageDigest.getInstance(algorithm).digest(data);
//        String format = "%0" + (digest.length*2) + "x%n";
//        System.out.printf(format, new BigInteger(1, digest));
        return 0;
    }
}