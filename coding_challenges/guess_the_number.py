import random

number = 0

def giveReward():
   print("Good Job!")


def isGuessCorrect(guess, answer):
    if (guess == answer):
        return True
    else:
        return False

def comeUpWithNumber():
    number = random.randint(1,10)
    print("I just came up with a number between 1 and 10 \n If you can guess the number you get a reward!")

def game():
   guess = int(input("So, what is your guess? \n"))
   if (isGuessCorrect(guess, number)):
      print("YES! You did it! \n Here's your reward \n")
      giveReward()
   else:
      print("HAHAHAHA, thats wrong!")
      game()
  
    

def main():
    comeUpWithNumber()
    game()

if __name__ == '__main__':
    main()