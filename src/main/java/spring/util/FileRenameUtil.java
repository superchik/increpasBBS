package spring.util;

import java.io.File;

public class FileRenameUtil {
	public static String checkSameFileName(String fileName, String path) { // 파일이름과 절대경로를 받아서 파일 여부 확인
		//인자인 fileName에서 확장자를 뺀 파일명 가려내자.
		// 우선 .의 위치를 알아낸다.
		
		int peride = fileName.lastIndexOf("."); // test123.txt
		
		String f_name = fileName.substring(0, peride); // test123
		
		String suffix = fileName.substring(peride); // .txt
		
		//전체 경로
		String saveFileName = path+System.getProperty("file.separator")+fileName;
		
		File f = new File(saveFileName);
		
		// 같은 이름이 잇을 경우 파일명 뒤에 숫자를 붙여준다.
		int idx = 1;
		while(f != null && f.exists()) {
			// 파일이 이미 존재하므로 이름을 변경하자
			StringBuffer sb = new StringBuffer();
			sb.append(f_name);
			sb.append(idx++); // 변경된 이름이 잇을 수도 있으므로 idx값을 일단 1증가시켜 둔다
			sb.append(suffix);
			
			fileName = sb.toString(); // test1231.txt
			
			saveFileName = path+System.getProperty("file.separator")+fileName;
			
			f = new File(saveFileName);
		}
		return fileName;
	}
}
