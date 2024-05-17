<template>
  <div class = "pt-5">
    <form method = "post" @submit.prevent="addNews()">
      <div class ="form-group">
        <label for="title">Title</label>
        <input style="margin-top: 10px;" required v-model="title" type="text" class="form-control" id="title" placeholder="Enter title">
      </div>
      <div>
        <label for="text" style="margin-top: 10px;">Content</label>
        <textarea style="margin-top: 10px;" required cols = "40" rows="5" v-model="content" type="text" class="form-control" id="text" placeholder="Enter content"></textarea>
      </div>
      <div class = "form-group">
        <br>
        <label for="tags">Tags</label>
        <input required v-model="tags" type="text" class = "form-control" id="tags">
      </div>
      <br>
      <div class = "row" style="text-align: center">
        <div class="col from-group">
          <b-form-select required v-model = "selectCategory" class = "m-3">
            <b-form-select-option v-for="category in categories" :key="category.name" :value = "category">{{category.name}}</b-form-select-option>
          </b-form-select>
        </div>
        <div class="col form-group">
          <b-form-select required v-model = "selectUsers" class="m-3">
            <b-form-select-option v-for="user in users" :key="user.email" :value="user">{{user.first_name}}</b-form-select-option>
          </b-form-select>
        </div>
      </div>
      <br><br><br>

      <div style="text-align: center">
        <button type = "submit" class = "btn btn-primary">
          Share news
        </button>
      </div>
    </form>

    <br><br>

    <table class="table text-center" style="width: 650px;margin-left:300px;">
      <thead>
        <tr>
          <th scope="col">Title</th>
          <th scope="col">Creation Date</th>
          <th scope="col">Content</th>
          <th scope="col">Delete</th>
          <th scope="col">Edit</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="news in newsList" :key="news.id">
          <b-card style = "margin-top: 10px">
            <td scope="row" @click="findNews(news.id)">{{news.title}}</td>
          </b-card>
          <td>{{new Date(news.creation_date).toISOString().split('T')[0]}}</td>
          <td>{{ news.content | shortText }}</td>
          <td scope="row">
            <b-button @click="deleteNews(news.id)" size ="sm">Delete</b-button>
          </td>
          <td>
            <b-button @click="editNews(news.id)" size="sm">Edit</b-button>
          </td>
        </tr>
      </tbody>

    </table>


  </div>
</template>

<script>
export default {
  name: "AddNews",
  filters:{
    shortText(value){
      if(value.length < 30){
        return value;
      }
      return value.slice(0,30) + '...'
    }
  },
  data(){
    return{
      date: '',
      title: null,
      content: null,
      categories: [],
      selectCategory: [],
      selectUsers: [],
      users: [],
      newsList: [],
      tags: null,
      tagList: []
    }
  },
  mounted(){
    this.$axios.get('/api/categories').then((response) => {
      this.categories = response.data;
    });
    this.$axios.get('/api/users').then((response) => {
      this.users = response.data;
    });
    this.$axios.get('/api/news').then((response)=>{
      this.newsList = response.data;
    });
  },
  methods: {
    deleteNews(id){
      this.$axios.delete(`api/news/${id}`).then(() => {
        this.newsList.splice(id, 1);
        window.location.reload();
      });
    },
    findNews(id){
      this.$router.push(`/news/${id}`);
    },
    editNews(id){
      this.$router.push(`/news/update/${id}`)
    },
    async addNews(){

      if (this.tags.includes(',')) {
        this.tagList = this.tags.split(',');
      } else {
        this.tagList = [this.tags];
      }


      let splitTag = [];
      for(let i = 0; i < this.tagList.length; i++){
        splitTag.push(this.tagList[i]);
      }

      for(let i = 0; i < splitTag.length; i++){
        await this.$axios.post('/api/tags', {
          "tag_name": splitTag[i]
        }).then(() => {
          //window.location.reload();
        })
      }

      console.log('title: '+this.title + ', content: '+ this.content + ', author: ' + this.author + ', category: ' + this.selectCategory);

      await this.$axios.post(`/api/news`, {
        "title": this.title,
        "content": this.content,
        "visits": 0,
        "author": this.selectUsers,
        "category": this.selectCategory,
        "tag": splitTag
      }).then(() => {
        //window.location.reload();
      });

    }

  }
}
</script>

<style scoped>

</style>