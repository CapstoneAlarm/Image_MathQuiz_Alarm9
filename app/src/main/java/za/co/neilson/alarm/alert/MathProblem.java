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
	private int max = 12;
	public MathProblem() {
		this(3);
	}

	public MathProblem(int numParts) {
		super();
		Random random = new Random(System.currentTimeMillis());

		//숫자 arraylist
		parts = new ArrayList<Integer>(numParts);
		for (int i = 0; i < numParts; i++)
			parts.add(i, (Integer) random.nextInt(max - min + 1) + min);

		//연산자 arraylist
		operators = new ArrayList<MathProblem.Operator>(numParts - 1);
		for (int i = 0; i < numParts - 1; i++)
			operators.add(i, Operator.values()[random.nextInt(2) + 1]);

		ArrayList<Object> combinedParts = new ArrayList<Object>();
		for (int i = 0; i < numParts; i++) {
			combinedParts.add(parts.get(i));
			if (i < numParts - 1)
				combinedParts.add(operators.get(i));
		}


		//sj
		//받아온 연산자에 따른 계산 수행

		while(combinedParts.contains(Operator.DIVIDE) ||combinedParts.contains(Operator.MULTIPLY)
				||combinedParts.contains(Operator.ADD) ||combinedParts.contains(Operator.SUBTRACT)){

			int i = 0;
			//Operator를 object형태로 combinedParts.get(i)에 넘겨주어 연산자를 식별할 수 있게함
			while(!(combinedParts.get(i) instanceof Operator)){
				i++;
			}

			if(combinedParts.get(i) == Operator.DIVIDE){
				answer = (Integer)combinedParts.get(i-1) / (Integer)combinedParts.get(i+1);
			}else if(combinedParts.get(i) == Operator.MULTIPLY){
				answer = (Integer)combinedParts.get(i-1) * (Integer)combinedParts.get(i+1);
			}else if(combinedParts.get(i) == Operator.ADD){
				answer = (Integer)combinedParts.get(i-1) + (Integer)combinedParts.get(i+1);
			}else {	//combinedParts.get(i) == Operator.SUBTRACT
				answer = (Integer)combinedParts.get(i-1) - (Integer)combinedParts.get(i+1);
			}

			for (int r = 0; r < 2; r++)
				combinedParts.remove(i-1);
			combinedParts.set(i-1, answer);
		}

	}

	//sj
	//문제 출력 ( a * b ) - c 이런 식으로
	@Override
	public String toString() {
		StringBuilder problemBuilder = new StringBuilder();

		problemBuilder.append("( ");
		problemBuilder.append(parts.get(0));
		problemBuilder.append(" ");
		problemBuilder.append(operators.get(0).toString());
		problemBuilder.append(" ");
		problemBuilder.append(parts.get(1));
		problemBuilder.append(" ) ");
		problemBuilder.append(operators.get(1).toString());
		problemBuilder.append(" ");
		problemBuilder.append(parts.get(2));
		problemBuilder.append(" ");

		return problemBuilder.toString();

	}

	public float getAnswer() {
		return answer;
	}

}