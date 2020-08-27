/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.packerina.cmd;


import io.ballerina.projects.utils.ProjectUtils;
import org.ballerinalang.tool.BLauncherCmd;
import picocli.CommandLine;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.ballerinalang.packerina.cmd.Constants.INIT_COMMAND;

/**
 * Init command for creating a ballerina project.
 */
@CommandLine.Command(name = INIT_COMMAND, description = "Create a Init Ballerina project")
public class InitCommand implements BLauncherCmd {

    private Path userDir;
    private PrintStream errStream;

    @CommandLine.Option(names = {"--help", "-h"}, hidden = true)
    private boolean helpFlag;

    @CommandLine.Parameters
    private List<String> argList;

    @CommandLine.Option(names = {"--list", "-l"})
    private boolean list = false;

    @CommandLine.Option(names = {"--template", "-t"}, description = "Acceptable values: [main, service, lib] " +
            "default: main")
    private String template = "main";

    public InitCommand() {
        userDir = Paths.get(System.getProperty("user.dir"));
        errStream = System.err;
        CommandUtil.initJarFs();
    }

    public InitCommand(Path userDir, PrintStream errStream) {
        this.userDir = userDir;
        this.errStream = errStream;
        CommandUtil.initJarFs();
    }

    @Override
    public void execute() {
        // If help flag is given print the help message.
        if (helpFlag) {
            String commandUsageInfo = BLauncherCmd.getCommandUsageInfo(INIT_COMMAND);
            errStream.println(commandUsageInfo);
            return;
        }
//        todo: verify this is required
        if (list) {
            errStream.println("Available templates:");
            for (String template : CommandUtil.getTemplates()) {
                errStream.println("    - " + template);
            }
            return;
        }

        // If the current directory is a ballerina project ignore.
        if (ProjectUtils.isProject(this.userDir)) {
            CommandUtil.printError(errStream,
                    "Directory is already a ballerina project",
                    null,
                    false);
            return;
        }

        // Check if one argument is given and not more than one argument.
        if (argList != null && !(1 == argList.size())) {
                CommandUtil.printError(errStream,
                        "too many arguments.",
                        "ballerina init <project-name>",
                        true);
                return;
        }

        // Check if there is a ballerina project in sub level.

        // Check if the command is executed inside a ballerina project
        Path projectRoot = ProjectUtils.findProjectRoot(this.userDir);
        if (projectRoot != null) {
            CommandUtil.printError(errStream,
                    "Directory is already within a ballerina project :" + projectRoot.toString(),
                    null,
                    false);
            return;
        }

        // Check if the template exists
        if (!CommandUtil.getTemplates().contains(template)) {
            CommandUtil.printError(errStream,
                    "Template not found, use `ballerina init --list` to view available templates.",
                    null,
                    false);
            return;
        }

        String packageName = this.userDir.getFileName().toString();
        if (argList != null && argList.size() > 0) {
            packageName = argList.get(0);
        }

        if (!ProjectUtils.validatePkgName(packageName)) {
            errStream.println("warning: invalid package name. Modified package name : " +
                    ProjectUtils.guessPkgName(packageName));
        }

        try {
            CommandUtil.initPackage(this.userDir, packageName, template);
        } catch (AccessDeniedException e) {
            errStream.println("error: Error occurred while initializing project : " + "Access Denied");
        } catch (IOException | URISyntaxException e) {
            errStream.println("error: Error occurred while initializing project : " + e.getMessage());
            return;
        }
        errStream.println("Ballerina project initialised ");
        errStream.println();
        errStream.println("Next:");
        errStream.println("    Use `ballerina create` to create a ballerina module.");
    }

    @Override
    public String getName() {
        return INIT_COMMAND;
    }

    @Override
    public void printLongDesc(StringBuilder out) {
        out.append("initialize a ballerina project in current directory");
    }

    @Override
    public void printUsage(StringBuilder out) {
        out.append("  ballerina init \n");
    }

    @Override
    public void setParentCmdParser(CommandLine parentCmdParser) {
    }
}
