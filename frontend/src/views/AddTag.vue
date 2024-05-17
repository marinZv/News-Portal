<template>

  <div class = "pt-5">
    <form method="post" v-on:submit.prevent="addTag()">
      <div class="form-group">
        <label for="name">Tag name</label>
        <input style="margin-top: 10px" required v-model="tag_name" type="text" class="form-control" id="name" placeholder="Enter name">
      </div>
      <br>
      <button type="submit" class="btn btn-primary mt-2">Add Tag</button>
    </form>

    <h1 class="mt-4">Tags</h1>
    <div class="row" style="display: inline;">
      <div class="col-4 mx-auto">
        <table class="table text-center" style="width: 650px;margin-left: -150px;">
          <thead>
            <tr>
              <th scope="col">Id</th>
              <th scope="col">Name</th>
              <th scope="col">Delete</th>
            </tr>
          </thead>

          <tbody>
<!--            <tr v-for="tag in tagList" :key="tag.id">-->
            <tr v-for="tag in paginatedTagList" :key="tag.id">
              <b-card style="margin-top: 10px">
                <td scope="row">{{tag.id}}</td>
              </b-card>
              <td scope="row">{{tag.tag_name}}</td>
              <td scope="row">
                <b-button v-if="tagList.length > 1" @click="deleteTag(tag.id)" size="sm">Delete</b-button>
              </td>
            </tr>
          </tbody>
        </table>

        <b-pagination
            v-model="currentPage"
            :total-rows="tagList.length"
            :per-page="perPage"
            align="center"
            size="sm"
            class="mt-3"
        ></b-pagination>


      </div>
    </div>

  </div>


</template>

<script>
export default {
  name: "AddTag",
  data(){
    return {
      date: '',
      tag_name: null,
      tagList: [],
      //
      currentPage: 1,
      perPage: 5,
      //
    }
  },
  mounted() {
    this.$axios.get('api/tags').then((response) => {
      this.tagList = response.data;
    });
  },
  methods: {
    addTag(){
      let exists = 0;
      for(let i = 0; i < this.tagList.length; i++){
        if(this.tag_name === this.tagList[i].tag_name){
          exists = 1;
        }
      }

      if(exists){
        alert("Tag sa ovim imenom vec postoji!Unesite drugo ime.");
      }else{
        this.$axios.post('/api/tags', {
          "tag_name":this.tag_name
        }).then(() => {
         // window.location.reload();
          this.$axios.get('api/tags').then((response) => {
            this.tagList = response.data;
          });
        })
      }
    },
    deleteTag(id){
      this.$axios.delete(`api/tags/${id}`).then(() => {
        this.tagList.splice(id, 1);
        this.$axios.get('api/tags').then((response) => {
          this.tagList = response.data;
        });
      });
      //window.location.reload();
    }
  },
  computed: {
    paginatedTagList() {
      const start = (this.currentPage - 1) * this.perPage;
      const end = start + this.perPage;
      return this.tagList.slice(start, end);
    },
  },
}
</script>

<style scoped>

</style>