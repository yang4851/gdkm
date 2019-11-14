package etc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;

//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {


	public static void main(String[] args) throws IOException {
		
//		ApplicationContext context = new ClassPathXmlApplicationContext("egovframework/spring/context-*.xml");
//		System.out.println(context.getBean("UserDAO").getClass().getName());
//		System.out.println(context.getBean("UserService").getClass().getName());

		File file = new File("C:/data/m170421_5.fastq");
		FileInputStream fis = new FileInputStream(file);
		
		int lineSize = 0; 
		
		String line = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		
		while((line = reader.readLine()) != null) {
			int mid = (line.length()/2);
			System.out.println("Length : " + line.length());
			System.out.println("Mid : " + mid);
			
			FileOutputStream fos = new FileOutputStream("C:/data/m170421_6.fastq");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
			writer.write(line.substring(0, mid));
			writer.close();
			
			fos = new FileOutputStream("C:/data/m170421_7.fastq");
			writer = new BufferedWriter(new OutputStreamWriter(fos));
			writer.write(line.substring(mid));
			writer.close();
		}
		
		System.out.println("Line : " + lineSize);
		reader.close();
	}
}
