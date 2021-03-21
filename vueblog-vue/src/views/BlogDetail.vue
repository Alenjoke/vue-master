<template>
  <div>
      <Header></Header>
      <div class="mdblog">
        <h2> {{ blog.title }}</h2>
        <el-link icon="el-icon-edit" v-if="own">
          <router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}">
          编辑</router-link></el-link>
        <el-divider></el-divider>
        <div class="markdown-body" v-html="blog.content"></div>
      </div>
  </div>
</template>

<script>
import Header from "../components/Header"
import "github-markdown-css"
export default {
  name: "BlogDetail",
  components: {
    Header,
  },
  data() {
    return {
      blog: {
        title: "默认",
        id: "",
        content: "内容"
      },
      own: false
    }
  },
  created() {

const blogId = this.$route.params.blogId
    const _this = this
    if(blogId) {
      _this.$axios.get('/blogs/' + blogId).then(res => {
        const blog = res.data.data

        _this.blog.id = blog.id
        _this.blog.title = blog.title

        var MardownIt = require("markdown-it")
        var md = new MardownIt()

        var result = md.render(blog.content)
        _this.blog.content = result


        _this.own = blog.userId ===_this.$store.getters.getUser.id
      })
    }
  }
}
</script>

<style>

.mdblog {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 100%;
  min-height: 700px;
  padding: 20px 15px;
}
</style>