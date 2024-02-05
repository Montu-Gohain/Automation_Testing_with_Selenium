
public class CoreJavaBrushup {
	public static void main(String[] args) {
		
	  int[] arr = new int[5]; // This is an array for storing 5 elements.
	  
	  arr_insert(arr, 5);
			
	  arr_print(arr,  5);
	  
	}
	public static void arr_insert(int[] arr, int size) {
		for(int i=0; i<size; i++) {
			arr[i] = (i+1)*10;
		}
	}
	public static void arr_print(int[] arr, int size) {
		for(int i=0; i<size; i++) {
			System.out.println(arr[i]);
		}
	}
}
