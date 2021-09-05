package net.gspatace.json.schema.podo.generator.cli.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "list", description = "List available generators")
public class ListCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Not implemented yet");
    }
}
