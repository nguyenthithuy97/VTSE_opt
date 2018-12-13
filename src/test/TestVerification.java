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
	static String KRATOS = "./benchmark/kratos/loop_10";
	public static void main(String[] args) throws RowsExceededException, WriteException, IOException {
		FileVerification fv = new FileVerification();
		
//		List<VerificationReport> reportList = fv.verify(new File(FLOAT_CDFPL + "/newton_1_1_true_unreach_call.c"));
		List<VerificationReport> reportList = fv.verify(new File(KRATOS + "/pc_sfifo_2.c"));
		for (VerificationReport report: reportList) {
			System.err.println(report.getCounterEx());
		}
		
//		File file = new File("TestSpoon.java");
//		
//		String extension = FilenameUtils.getExtension(file.getAbsolutePath());
//		String name = FilenameUtils.getName(file.getAbsolutePath());
//		name = FilenameUtils.getBaseName(file.getAbsolutePath());
//		System.out.println("extension: " + extension);
//		System.out.println("name: " + name);
	}
}
