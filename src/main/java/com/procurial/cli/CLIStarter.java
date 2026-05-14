package com.procurial.cli;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Completer;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.Reference;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.builtins.Completers;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Paths;

import org.jline.reader.UserInterruptException;

public class CLIStarter {
    final static CommandExecutor executor = new CommandExecutor();

    public static void main(String[] args) {
         try {
            // Puts the Linux terminal into "raw" mode for live key tracking
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            Completer fileCompleter = new Completers.FilesCompleter(Paths.get(""));
            Completer dirCompleter = new Completers.DirectoriesCompleter(Paths.get(""));
            Completer aggregateCompleter = new AggregateCompleter(
                new StringsCompleter("help", "exit", "clean", "save", "autoJudge", "setDefaultTime", "setDefaultScore", "setDefaultContest", "setupWti"),
                new ArgumentCompleter(new StringsCompleter("run"), fileCompleter),
                new ArgumentCompleter(new StringsCompleter("load"), fileCompleter),
                new ArgumentCompleter(new StringsCompleter("new"), dirCompleter),
                new ArgumentCompleter(new StringsCompleter("add"), new StringsCompleter("account", "problem"), dirCompleter, fileCompleter),
                new ArgumentCompleter(new StringsCompleter("setNewPasscodes"), fileCompleter)
                // new ArgumentCompleter(new StringsCompleter("display"), new StringsCompleter("account", "problem"), fileCompleter)
            );

            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .variable(LineReader.HISTORY_FILE, Paths.get("~/.pc2lib3_history"))
                    .history(new DefaultHistory())
                    .completer(aggregateCompleter)
                    .build();

            KeyMap<Binding> keyMap = reader.getKeyMaps().get(LineReader.MAIN);
            keyMap.bind(new Reference(LineReader.BACKWARD_KILL_WORD),
                KeyMap.ctrl('H'), // Many terminals interpret Ctrl+Backspace as Ctrl+H (\x08)
                "\\b",            // Backspace literal
                "\\x1f"           // Unit Separator (sent by some modern Windows terminals)
            );

            while (true) {
                String command = reader.readLine("PC^2 PQA CLI>");
                String[] arguments = command.split(" ");

                executor.executeCommand(arguments);
            }
        } catch(UserInterruptException e) {} catch (IOException e) {
            e.printStackTrace();
        }
    }
}
