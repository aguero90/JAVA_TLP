package lezione4;

public class Continue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int res = 0;
		for (int i = 0,j = 10; i<j; i++,j--) {
		  if (i == 3) {
			  continue;
		  }
		  res += i+j;
		}
		System.out.println(res);

	}

}
