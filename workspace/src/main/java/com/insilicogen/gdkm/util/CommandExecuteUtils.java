package com.insilicogen.gdkm.util;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

public class CommandExecuteUtils {

	public static String executeStdOut(String executable) throws Exception {
		return executeStdOut(CommandLine.parse(executable));
	}
	
	public static String executeStdOut(CommandLine command) throws Exception {
		String output = "";
		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
		executor.setStreamHandler(streamHandler);
		
		try {
			executor.execute(command);
			output = baos.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (Exception e) {}
		}
	
		return output;
	}
	
	public static void main(String[] args) {
		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
		executor.setStreamHandler(streamHandler);
		
//		String command = "C:/Tools/clc-assembly-cell-4.2.2/clc_sequence_info.exe -n -r C:/workspace_kisti/igda/samples/test_01.fastq";
//		CommandLine commandLine = CommandLine.parse(command);
//		try {
//			int exit = executor.execute(new CommandLine(commandLine));
//			String output = baos.toString();
//			
//			System.out.println("exit = " + exit);
//			System.out.println("output = " + output);
//			
//			
//		} catch (Exception e) {
//			System.out.println("##### " + e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				baos.close();
//			} catch (Exception e) {}
//		}
		
//		CommandLine command = new CommandLine("C:/Tools/clc-assembly-cell-4.2.2/clc_sequence_info.exe");
//		command.addArgument("-n");
//		command.addArgument("-r");
//		command.addArgument("C:/workspace_kisti/igda/samples/test_01.fastq");
//		
//		System.out.println(command.toString());
//		
//		try {
//			int exit = executor.execute(command);
//			String output = baos.toString();
//			
//			System.out.println("exit = " + exit);
//			System.out.println("output = " + output);
//			
//			
//		} catch (Exception e) {
//			System.out.println("##### " + e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				baos.close();
//			} catch (Exception e) {}
//		}
		
		File executable = new File(EgovProperties.getProperty("TOOL.FASTQC.PATH"));
		CommandLine command = new CommandLine("perl");
		command.addArgument(executable.getAbsolutePath());
		command.addArgument("C:/workspace_2018/gdkm/samples/test_01.fastq");
		command.addArgument("-o");
		command.addArgument("C:/workspace_2018/gdkm/samples/test_01_fastqc");
		
		System.out.println(command.toString());
		
		try {
			int exit = executor.execute(command);
			String output = baos.toString();
			
			System.out.println("exit = " + exit);
			System.out.println("output = " + output);
			System.out.println("result = " + output.indexOf("complete"));
			
			
		} catch (Exception e) {
			System.out.println("##### " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (Exception e) {}
		}
	}
}
