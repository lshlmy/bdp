package com.lshlmy.hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MapReduceTest {

    private static final Logger logger = LoggerFactory.getLogger(MapReduceTest.class);

    public static class TokenizerMapper extends
            Mapper<Object, Text, Text, IntWritable> {


        public static final IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                this.word.set(itr.nextToken());
                context.write(this.word, one);
            }
        }

    }

    public static class IntSumReduce extends
            Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            IntWritable val;
            for (Iterator i = values.iterator(); i.hasNext(); sum += val.get()) {
                val = (IntWritable) i.next();
            }
            this.result.set(sum);
            context.write(key, this.result);
        }
    }

    @Test
    public void test() throws IOException {
        System.setProperty("hadoop.home.dir", "D:\\文档\\hadoop");
        System.setProperty("HADOOP_USER_NAME", "lshlmy");
        String[] ioArgs = new String[]{"/test/test_in/test1.txt", "/test/test_out"};
        Configuration conf = new JobConf(new Configuration(), MapReduceTest.class);
        conf.setQuietMode(false);
        //conf.set("mapred.job.tracker", "192.168.78.222");
        String[] otherArgs = new GenericOptionsParser(conf, ioArgs).getRemainingArgs();

        FileSystem fs = FileSystem.get( conf);
        Path outPath = new Path("hdfs://192.168.78.222:9000/test/test_out");
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }

        if (otherArgs.length != 2) {
            System.err.println("Usage: Data Deduplication <in> <out>");
            System.exit(2);
        }

        try {
            Job job = Job.getInstance(conf, "Data Deduplication");

            //设置Map、Combine和Reduce处理类
            //job.setJar("D:/project/Idea/bdp/hadoop/build/libs/hadoop-1.0-SNAPSHOT.jar");
            //job.setCombinerClass(Reduce.class);

            //job.setJarByClass(MapReduceTest.class);
            job.setMapperClass(MapReduceTest.TokenizerMapper.class);
            job.setReducerClass(MapReduceTest.IntSumReduce.class);


            //设置输出类型

            job.setOutputKeyClass(Text.class);

            job.setOutputValueClass(IntWritable.class);


            //设置输入和输出目录

            FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

            FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);

        } catch (Exception e) {
            logger.error("执行错误", e);
        } finally {
            System.out.println("完成");
            IOUtils.closeStream(fs);
        }
    }
}
