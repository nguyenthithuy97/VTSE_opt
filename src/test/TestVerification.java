package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import app.verification.FileVerification;
import app.verification.report.VerificationReport;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class TestVerification {
	static String FLOAT_CDFPL = "./benchmark/float-cdfpl-func";
	static String FLOAT_BENCHS = "./benchmark/float-benchs";
	static String KRATOS = "./benchmark/kratos/loop_5";
	static String EXAMPLE = "./benchmark/example";
	public static void main(String[] args) throws RowsExceededException, WriteException, IOException {
		FileVerification fv = new FileVerification();
//		List<VerificationReport> reportList = fv.verify(new File(EXAMPLE + "/example_4.c"));
		List<VerificationReport> reportList = fv.verify(new File(FLOAT_CDFPL + "/newton_3_1_true_unreach_call.c"));
//		List<VerificationReport> reportList = fv.verify(new File(KRATOS + "/transmitter_1.c"));
//		List<VerificationReport> reportList = fv.verify(new File(KRATOS + "/pc_sfifo_3.c"));
		for (VerificationReport report: reportList) {
			System.err.println(report.getCounterEx());
		}
		System.out.println("DONE");
		
//		File file = new File("TestSpoon.java");
//		
//		String extension = FilenameUtils.getExtension(file.getAbsolutePath());
//		String name = FilenameUtils.getName(file.getAbsolutePath());
//		name = FilenameUtils.getBaseName(file.getAbsolutePath());
//		System.out.println("extension: " + extension);
//		System.out.println("name: " + name);
	}
}
