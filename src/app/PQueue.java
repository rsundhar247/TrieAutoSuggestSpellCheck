package app;

public class PQueue implements Comparable<PQueue> {

	private int weight;
	private String word;
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	public PQueue(int weight, String word){
		setWeight(weight);
		setWord(word);
	}
	
	public boolean equals(PQueue o){
		return (this.getWeight() == o.getWeight());
	}
	
	public int compareTo(PQueue o){
		if(this.equals(o))
			return 0;
		else if(getWeight() < o.getWeight())
			return 1;
		else 
			return -1;
	}
	
	
	
}
