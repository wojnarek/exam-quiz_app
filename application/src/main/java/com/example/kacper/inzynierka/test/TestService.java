package com.example.kacper.inzynierka.test;

import com.example.kacper.inzynierka.questions.Question;
import com.example.kacper.inzynierka.solve.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    private TestRepository testRepository;

    private MongoOperations mongoOperations;

    @Autowired
    public TestService(TestRepository testRepository, MongoOperations mongoOperations){
        this.testRepository = testRepository;
        this.mongoOperations =mongoOperations;
    }

    public Test getOne(String id){
        Optional<Test> Otest = testRepository.findById(id);
        Test test = Otest.get();
        return test;
    }

    public List<Test> getAllNotHideTests(String owner){;

        Query query = new Query(new Criteria("owner").is(owner).and("hide").is(false));
        return mongoOperations.find(query, Test.class);
    }

    public Test getToEditTest(String id, String owner){
        Query query = new Query(new Criteria("id").is(id).and("owner").is(owner));
        return mongoOperations.findOne(query,Test.class);
    }

    public Test getTestId(String test_code){
        Query query = new Query(new Criteria("test_code").is(test_code));
        query.fields().include("id");
        return mongoOperations.findOne(query,Test.class);
    }

    public boolean updateDate(String id, LocalDateTime new_start_date, LocalDateTime new_end_date){
       try {
           Query query = new Query(new Criteria("id").is(id));
           Update update = new Update().set("start_date",new_start_date).set("end_date",new_end_date);
           mongoOperations.updateFirst(query,update, Test.class);
           return true;
       }catch (Exception e){
           e.printStackTrace();
       }
        return false;
    }

    public boolean updateName(String id, String newName){
        try {
            Query query = new Query(new Criteria("id").is(id));
            Update update = new Update().set("name",newName);
            mongoOperations.updateFirst(query,update,Test.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDescription(String id, String newDescription){
        try {
            Query query = new Query(new Criteria("id").is(id));
            Update update = new Update().set("description", newDescription);
            mongoOperations.updateFirst(query,update,Test.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCategory(String id, String newCategory){
        try {
            Query query = new Query(new Criteria("id").is(id));
            Update update = new Update().set("category", newCategory);
            mongoOperations.updateFirst(query,update,Test.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassPercent(String id, int newPasspercent){
        try {
            Query query = new Query(new Criteria("id").is(id));
            Update update = new Update().set("pass_percent", newPasspercent);
            mongoOperations.updateFirst(query,update,Test.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTimeForAnswer(String id, double newTime){
        try {
            Query query = new Query(new Criteria("id").is(id));
            Update update = new Update().set("time_for_answer", newTime);
            mongoOperations.updateFirst(query,update,Test.class);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Test getTestDate(String test_code){
        Query query = new Query(new Criteria("test_code").is(test_code));
        query.fields().include("start_date","end_date");
        if(mongoOperations.findOne(query,Test.class) == null){
            return new Test();
        }
        return mongoOperations.findOne(query,Test.class);
    }

    public Test getTestStartDate(String test_id){
        Query query = new Query(new Criteria("id").is(test_id));
        query.fields().include("start_date");
        return mongoOperations.findOne(query,Test.class);
    }

    public int getTimeToAnswer(String test_id){
        Query query = new Query(new Criteria("id").is(test_id));
        query.fields().include("time_for_answer");
        List<Test> t = mongoOperations.find(query,Test.class);
        int time = (int) t.get(0).getTime_for_answer();
            return time;
    }

    public String generateTestCode(){
        String test_code;
        do{
          test_code = Test.testCodeGenerator();
        }while(!testRepository.findExistingCode(test_code).isEmpty());

        return test_code;
    }

    public void hideTest(String test_id, String owner){
        Update update = new Update();
        update.set("hide",true);
        Query query = new Query(new Criteria("id").is(test_id).and("owner").is(owner));
        mongoOperations.findAndModify(query,update,Test.class);
    }

    public void unhideTest(String test_id, String owner){
        Update update = new Update();
        update.set("hide",false);
        Query query = new Query(new Criteria("id").is(test_id).and("owner").is(owner));
        mongoOperations.findAndModify(query,update,Test.class);
    }

    public Test getTestDetails(String test_id){
        Query query = new Query(new Criteria("id").is(test_id));
        query.fields().include("name","pass_percent","time_for_answer","question_count","start_date","end_date");
        return mongoOperations.findOne(query,Test.class);
    }

    public Test getTestNameAndIdAndPass(String test_id, String owner){
        Query query = new Query(new Criteria("id").is(test_id).and("owner").is(owner));
        query.fields().include("name","id","pass_percent");
        return mongoOperations.findOne(query,Test.class);
    }

    public boolean isTestActive(String test_id){
        Query query = new Query(new Criteria("id").is(test_id));
        query.fields().include("start_date","end_date");
        Test test = mongoOperations.findOne(query,Test.class);
        if(LocalDateTime.now().isAfter(test.getStart_date()) && LocalDateTime.now().isBefore(test.getEnd_date())){
            return true;
        }
        return false;
    }

    public List<Test> findTest(String looking_for){
        Query query = new Query(new Criteria("name").regex(looking_for));
       return mongoOperations.find(query, Test.class);
    }

    public List<Test> getLatest(String owner){
        Query query = new Query(new Criteria("owner").is(owner)).with(Sort.by(Sort.Direction.DESC,"id")).limit(5);
        return mongoOperations.find(query,Test.class);
    }

}
