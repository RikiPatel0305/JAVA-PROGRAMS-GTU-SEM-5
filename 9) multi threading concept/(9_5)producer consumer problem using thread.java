import java.util.*;
class Producer implements Runnable
{
   private final List<Integer> taskQueue;
   private final int           MAX_CAPACITY;
 
   public Producer(List<Integer> sharedQueue, int size)
   {
      this.taskQueue = sharedQueue;
      this.MAX_CAPACITY = size;
   }
 
   @Override
   public void run()
   {
      int counter = 0;
      while (true)
      {
         try
         {
            produce(counter++);
         }
         catch (InterruptedException ex)
         {
            ex.printStackTrace();
         }
      }
   }
 
   private void produce(int i) throws InterruptedException
   {
      synchronized (taskQueue)
      {
         while (taskQueue.size() == MAX_CAPACITY)
         {
            System.out.println("Queue is full " + Thread.currentThread().getName() + " is waiting , size: " + taskQueue.size());
            taskQueue.wait();
         }
           
         Thread.sleep(70);
         taskQueue.add(i);
         System.out.println("Produced: " + (i%10));
         taskQueue.notifyAll();
      }
   }
}

class Consumer implements Runnable
{
   private final List<Integer> taskQueue;
 
   public Consumer(List<Integer> sharedQueue)
   {
      this.taskQueue = sharedQueue;
   }
 
  
   public void run()
   {
      while (true)
      {
         try
         {
            consume();
         } catch (InterruptedException ex)
         {
            ex.printStackTrace();
         }
      }
   }
 
   private void consume() throws InterruptedException
   {
      synchronized (taskQueue)
      {
         while (taskQueue.isEmpty())
         {
            System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting , size: " + taskQueue.size());
            taskQueue.wait();
         }
         Thread.sleep(70);
         int i = (Integer) taskQueue.remove(0);
         System.out.println("Consumed: " + (i%10));
         taskQueue.notifyAll();
      }
   }
}


class ProducerConsumerProblem
{
	public static void main(String[] args) {
		List<Integer> taskQueue=new ArrayList<Integer>();
		int MAX_VALUE=10;
		Thread tProducer=new Thread(new Producer(taskQueue,MAX_VALUE),"Producer");
		Thread tConsumer=new Thread(new Consumer(taskQueue),"Consumer");
		tProducer.start();
		tConsumer.start();
	}
}