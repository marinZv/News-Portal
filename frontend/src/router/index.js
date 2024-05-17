import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
// import alert from "alert";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/news/visits',
    name: 'TopNews',
    meta:{
      authRequired: false
    },
    component: () => import('../views/TopNews.vue')
  },
  {
    path: '/news',
    name: 'NewsArticle',
    meta: {
      authRequired: false,
    },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/NewsArticle.vue')
  },
  {
    path: '/news/:id',
    name: 'SingleNews',
    meta: {
      authRequired: false
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/SingleNews.vue')
  },
  {
    path: '/news/tag/:id',
    name: 'NewsByTag',
    meta: {
      authRequired: false,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/NewsByTag.vue')
  },
  {
    path: '/news/category/:name',
    name: 'NewsByCategory',
    meta: {
      authRequired: false,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/NewsByCategory.vue')
  },
  {
    path: '/users/login',
    name: 'Login',
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/news',
    name: 'AddNews',
    meta: {
      authRequired: true,
      statusRequired: true
    },

    component: () => import(/* webpackChunkName: "about" */ '../views/AddNews.vue')
  },
  {
    path: '/category',
    name: 'AddCategory',
    meta: {
      authRequired: true,
      statusRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/AddCategory.vue')
  },
  {
    path: '/tags',
    name: 'AddTag',
    meta: {
      authRequired: true,
      statusRequired: true
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/AddTag.vue')
  },
  {
    path: '/users',
    name: 'AddUser',
    meta: {
      authRequired: true,
      adminRequired: true
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/AddUser.vue')
  },
  {
    path: '/users/:email',
    name: 'SingleUser',
    meta: {
      authRequired: true,
      adminRequired: true,
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/SingleUser.vue')
  },
  {
    path: '/category/:name',
    name: 'EditCategory',
    meta: {
      authRequired: true,
      statusRequired: true
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/EditCategory.vue')
  },
  {
    path: '/news/update/:id',
    name: 'EditNews',
    meta: {
      authRequired: true,
      statusRequired: true
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/EditNews.vue')
  },

]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  if(to.meta.authRequired) {
    const jwt = localStorage.getItem("jwt");
    if (!jwt) {
      next({name: 'Login'});
      return;
    }


    const payload = JSON.parse(atob(jwt.split('.')[1]));
    const expDate = new Date(payload.exp * 1000);

    const role = payload.role;
    console.log(role);

    if (to.meta.adminRequired) {
      if (role != 1) {
        alert("Only admin can take this action!");
        return;
      }
    }

    const status = payload.status;

    console.log("status: " + payload.status);

    if(to.meta.statusRequired){
      if(status != 1){
        alert("Your status is inactive! You cannot acces to CMS");
        return;
      }
    }

    if (expDate < new Date()) {
      next({name: 'Login'});
      return;
    }
  }

  next();

})

export default router
