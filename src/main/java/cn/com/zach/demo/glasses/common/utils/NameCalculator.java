package cn.com.zach.demo.glasses.common.utils;

/**
 * 名称转换
 *
 */
public class NameCalculator{
	
	/**
	 * 名称转换规则是:首字母小写,略过以特殊字符和数字开始的字符,首字母以后只略过特殊字符(字母和数字和下划线)
	 * 例如 : calculator(false,"hello","word") 输出: helloWord
	 *      calculator(false,"h!e@l#l$o&","word") 输出: helloWord
	 *      calculator(false,"hello_","word") 输出: hello_Word
	 * @param underline 是否添加下划线
	 * @param input
	 * @return
	 */
	public static String calculator(String ...input){
		StringBuilder answer = new StringBuilder(16);
		if( input.length > 0){
			boolean flag=false;
			char  c = 0;
			String s = null;
			for(int pos =0;pos<input.length;pos++ ){
				s = input[pos];
				if( s != null && s.length() > 0 ){
					char chars[] = s.toCharArray() ;
					if( pos>0 && chars[0]>=97 && chars[0]<=122){ // a=97 z=122
						chars[0] = (char) (chars[0] - 32);//ASCII中大写字母和小写字母相差32
					}
					for(int i =0; i < chars.length;i++){
						c = chars[i];
						if(  c <= 47 || ( c >= 58 && c<= 64) || ( c>=91 && c<=96) || (c>=123 && c<=127)){
							flag = true;
							continue;
						}
						if( flag && (c>=97 && c<=122)){
							c = (char) (c - 32);//ASCII中大写字母和小写字母相差32
							flag=false;
						}
						answer.append( c );
					}
				}
			}
		}
		return answer.toString();
	}
	
	public static void main(String []args){
		System.out.println(NameCalculator.calculator("he中文llo_word","word"));
	}
}
