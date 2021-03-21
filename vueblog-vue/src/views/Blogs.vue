<template>
  <div>
    <Header></Header>

    <el-timeline>
      <el-timeline-item
        :timestamp="blog.created"
        placement="top"
        v-for="blog in blogs"
        :key="blog.id"
      >
        <el-card>
          <h4>
            <router-link
              :to="{ name: 'BlogDetail', params: { blogId: blog.id } }"
            >
              {{ blog.title }}</router-link
            >
          </h4>
          <p>{{ blog.description }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <el-pagination
      class="mpage"
      background
      layout="prev, pager, next"
      :current-page="cur"
      :page-size="pageSize"
      :total="total"
      @current-change="page"
    >
    </el-pagination>
  </div>
</template>

<script>
import Header from "../components/Header";
export default {
  name: "Blogs.vue",
  data() {
    return {
      blogs: {},
      cur: 1,
      total: 0,
      pageSize: 5,
    };
  },
  components: { Header },
  methods: {
    page(cur) {
      const _this = this;
      let url = `/blogs?currentPage=${cur}&size=${_this.pageSize}`;
      _this.$axios.get(url).then((res) => {
        console.log(res);
        _this.blogs = res.data.data.records;
        _this.cur = res.data.data.current;
        _this.total = res.data.data.total;
        _this.size = res.data.data.size;
      });
    },
  },
  created() {
    this.page(1);
  },
};
</script>

<style>
.mpage {
  margin: 0 auto;
  text-align: center;
}
</style>