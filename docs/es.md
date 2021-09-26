### 常用操作
- 查看基础信息  
```
curl http://192.168.10.105:9200

{
     "name" : "ibh-es-1",
     "cluster_name" : "ibh-es",
     "cluster_uuid" : "glYnFhk3QHGyKWNhwXwnDQ",
     "version" : {
            "number" : "6.5.1",
            "build_flavor" : "default",
            "build_type" : "tar",
            "build_hash" : "8c58350",
            "build_date" : "2018-11-16T02:22:42.182257Z",
            "build_snapshot" : false,
            "lucene_version" : "7.5.0",
            "minimum_wire_compatibility_version" : "5.6.0",
            "minimum_index_compatibility_version" : "5.0.0"
     },
     "tagline" : "You Know, for Search"
}
```
- 集群运行状况
```
curl -X GET http://192.168.10.105:9200/_cat/health?v

epoch      timestamp cluster status node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent
1544096747 11:45:47  ibh-es  yellow          1         1      5   5    0   0     5           0                -               50.0%      

```
- 获得群集中的节点列表
```
curl -X GET http://192.168.10.105:9200/_cat/nodes?v

ip               heap.percent ram.percent cpu load_1m load_5m load_15m node.role master name
192.168.10.105           22          96    0    0.00    0.02     0.05 mdi       *      ibh-es-1
```
- 查看当前节点的所有Index
```
curl -X GET http://192.168.10.105:9200/_cat/indices?v

health status index uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   blog  Htgn3UiMQR6NzNqM1U1RuA   5   1          0            0      1.2kb          1.2kb
```
- 新建 Index
```
curl -X PUT http://192.168.10.105:9200/kh_ydkh_sz?pretty

{
  "acknowledged" : true,
  "shards_acknowledged" : true,
  "index" : "customer"
}
```
- 新建 Document
```
curl -X PUT "192.168.10.105:9200/customer/_doc/1?pretty" -H 'Content-Type: application/json' -d'
{
  "name": "John Doe"
}
'

response:
{
  "_index" : "customer",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 0,
  "_primary_term" : 1
}
```
- 查询 Document
```
curl -X GET "localhost:9200/kh_ydkh_sz/ydkh/0800030039446740?pretty"

{
  "_index" : "customer",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "found" : true,
  "_source" : {
    "name" : "John Doe"
  }
}
```
- 删除 Index
```
curl -X DELETE "localhost:9200/kh_ydkh_sz?pretty"

{
  "acknowledged" : true
}
```
- 查看mapping
```
curl -X GET localhost:9200/kh_ydkh_sz/_mapping/doc?pretty
```
- 创建index mapping
```
curl -X PUT "localhost:9200/kh_ydkh_gx2?pretty" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "doc": {
      "properties": {
        "yddz": {
          "type": "text", 
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        }, 
        "yhmc": {
          "type": "text", 
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        }
      }
    }
  }
}
'
```  
- 查询
```
curl -XPOST http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty  -H 'Content-Type:application/json' -d'
{
    "query" : { "match" : { "yhmc" : "安桂华" }},
    "_source": ["yhmc", "yddz", "yhbh"]
}
'
```

### 安装ik分词器插件
- 解压至插件目录
```
wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.5.1/elasticsearch-analysis-ik-6.5.1.zip
mkdir ik && unzip elasticsearch-analysis-ik-6.5.1.zip  -d ik
cp -r ik/ /home/es/app/elasticsearch-6.5.1/plugins/
```
- 重启es  
kill掉， -d 后台启动
- 测试
```
1.create a index
curl -XPUT http://localhost:9200/index

2.create a mapping
curl -XPOST http://localhost:9200/index/fulltext/_mapping -H 'Content-Type:application/json' -d'
{
        "properties": {
            "content": {
                "type": "text",
                "analyzer": "ik_max_word",
                "search_analyzer": "ik_max_word"
            }
        }
}'

3.index some docs
curl -XPOST http://localhost:9200/index/fulltext/1 -H 'Content-Type:application/json' -d'
{"content":"美国留给伊拉克的是个烂摊子吗"}
'
curl -XPOST http://localhost:9200/index/fulltext/3 -H 'Content-Type:application/json' -d'
{"content":"中韩渔警冲突调查：韩警平均每天扣1艘中国渔船"}
'

4.query
curl -XPOST http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty  -H 'Content-Type:application/json' -d'
{
    "query" : { "match" : { "yhmc" : "安桂华" }},
    "_source": ["yhmc", "yddz", "yhbh"]
}
'
```

### 各种查询
- 基本匹配查询(Basic Match Query)
```
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "match" : {
            "yhmc" : "安桂华"
        }
    },
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"],
    "highlight": {
        "fields" : {
            "yhmc" : {}
        }
    }
}'
```
- 多字段搜索(Multi-field Search)
```$xslt
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "multi_match" : {
            "query" : "安桂华",
            "fields": ["yhmc", "yddz"]
        }
    },
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```
- 多字段加权重搜索 Boosting
```$xslt
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "multi_match" : {
            "query" : "安桂华",
            "fields": ["yhmc^3", "yddz"]
        }
    },
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```
- 布尔查询(Bool Query)  
布尔查询可以接受一个must参数(等价于AND)，一个must_not参数(等价于NOT)，以及一个should参数(等价于OR)。
```
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "bool": {
            "must": {
                "bool" : { "should": [
                      { "match": { "yhmc": "安桂华" }},
                      { "match": { "yddz": "天景园" }} ] }
            },
            "must_not": { "match": {"yddz": "202" }}
        }
    },
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```
- Fuzzy Queries（模糊查询）  
模糊查询可以在Match和Multi-Match查询中使用以便解决拼写的错误（对中文无效）
```
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "multi_match" : {
            "query" : "安桂花",
            "fields": ["yhmc"],
            "fuzziness": "AUTO"
        }
    },
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```
- Wildcard Query(通配符查询)  
通配符查询允许我们指定一个模式来匹配，而不需要指定完整的trem。  
?将会匹配任何字符；*将会匹配零个或者多个字符。（对中文无效）
```
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "wildcard" : {
            "yhmc" : "安桂?"
        }
    },
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```
- Regexp Query(正则表达式查询)  
（对中文无效）
```
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "regexp" : {
            "yddz" : "振宁花园[6|六]栋[2|二]单元"
        }
    },
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```
- Match Phrase Query(匹配短语查询)
```$xslt
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "multi_match": {
            "query": "振宁花园", 
            "fields": ["yddz"], 
            "type": "phrase", 
            "slop": 1
        }
    }, 
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```
- Filtered Query(过滤查询)
```
curl -XGET http://localhost:9200/kh_ydkh_gx2/doc/_search?pretty -H 'Content-Type:application/json' -d '
{
    "query": {
        "multi_match": {
            "query": "振宁花园", 
            "fields": ["yddz"], 
            "type": "phrase", 
            "slop": 1
        }
    }, 
    "size": 5,
    "from": 0,
    "_source": ["yhmc", "yddz", "yhbh"]
}'
```