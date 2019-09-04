import Vue from 'vue'
import Router from 'vue-router'
import Users from './views/Users.vue'

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'home',
            component: Users
        },
        {
            path: '/users/logs',
            name: 'users-logs',
            component: () => import('./views/Userslogs.vue')
        },
        {
            path: '/users/:id/logs',
            name: 'user-logs',
            component: () => import('./views/Userlogs.vue')
        },
    ]
})
