/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * I modified part of the contents in Korean.
 * In addition, the menu was added in the scope without erasing the contents.
 * The source of this source is "https://github.com/SheldonNeilson/Android-Alarm-Clock.git"
 */
package za.co.neilson.alarm.alert;

import java.util.ArrayList;
import java.util.Random;

public class MathProblem {

	enum Operator {
		ADD, SUBTRACT, MULTIPLY, DIVIDE;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			String string = null;
			switch (ordinal()) {
			case 0:
				string = "+";
				break;
			case 1:
				string = "-";
				break;
			case 2:
				string = "*";
				break;
			case 3:
				string = "/";
				break;
			}
			return string;
		}
	}

	private ArrayList<Integer> parts;
	private ArrayList<Operator> operators;
	private int answer = 0;
	private int min = 0;
	private int max = 10;
	public MathProblem() {		
		this(3);
	}
	
	public MathProblem(int numParts) {
		super();
		Random random = new Random(System.currentTimeMillis());

		parts = new ArrayList<Integer>(numParts);
		for (int i = 0; i < numParts; i++)
			parts.add(i, (Integer) random.nextInt(max - min + 1) + min);
		
		operators = new ArrayList<MathProblem.Operator>(numParts - 1);
		for (int i = 0; i < numParts - 1; i++)
			operators.add(i,Operator.values()[random.nextInt(2)+1]);
		
		ArrayList<Object> combinedParts = new ArrayList<Object>();
		for (int i = 0; i < numParts; i++){
			combinedParts.add(parts.get(i));
			if(i<numParts-1)
				combinedParts.add(operators.get(i));
		}
		
		while(combinedParts.contains(Operator.DIVIDE)){	
			int i = combinedParts.indexOf(Operator.DIVIDE);
			answer = (Integer) combinedParts.get(i-1) / (Integer) combinedParts.get(i+1);
			for (int r = 0; r < 2; r++)
				combinedParts.remove(i-1);
			combinedParts.set(i-1, answer);
		}
		while(combinedParts.contains(Operator.MULTIPLY)){	
			int i = combinedParts.indexOf(Operator.MULTIPLY);
			answer = (Integer) combinedParts.get(i-1) * (Integer) combinedParts.get(i+1);
			for (int r = 0; r < 2; r++)
				combinedParts.remove(i-1);
			combinedParts.set(i-1, answer);			
		}
		
//		while(combinedParts.contains(Operator.ADD) ||combinedParts.contains(Operator.SUBTRACT)){	
//			int i = 0;
//			while(!(combinedParts.get(i) instanceof Operator)){
//				i++;
//			}
//			if(combinedParts.get(i) == Operator.ADD){
//				answer = (Integer)combinedParts.get(i-1) + (Integer)combinedParts.get(i+1);
//			}else{
//				answer = (Integer)combinedParts.get(i-1) - (Integer)combinedParts.get(i+1);
//			}
//			for (int r = 0; r < 2; r++)
//				combinedParts.remove(i-1);
//			combinedParts.set(i-1, answer);
//		}
		
		while(combinedParts.contains(Operator.ADD)){	
			int i = combinedParts.indexOf(Operator.ADD);
			answer = (Integer) combinedParts.get(i-1) + (Integer) combinedParts.get(i+1);
			for (int r = 0; r < 2; r++)
				combinedParts.remove(i-1);
			combinedParts.set(i-1, answer);
		}
		while(combinedParts.contains(Operator.SUBTRACT)){	
			int i = combinedParts.indexOf(Operator.SUBTRACT);
			answer = (Integer) combinedParts.get(i-1) - (Integer) combinedParts.get(i+1);
			for (int r = 0; r < 2; r++)
				combinedParts.remove(i-1);
			combinedParts.set(i-1, answer);
		}
		
//		2 5 7 8 9 11
//		 + - * / -
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder problemBuilder = new StringBuilder();
		for (int i = 0; i < parts.size(); i++) {
			problemBuilder.append(parts.get(i));
			problemBuilder.append(" ");
			if (i < operators.size()){
				problemBuilder.append(operators.get(i).toString());
				problemBuilder.append(" ");
			}
		}
		return problemBuilder.toString();
	}

	public float getAnswer() {
		return answer;
	}

}
