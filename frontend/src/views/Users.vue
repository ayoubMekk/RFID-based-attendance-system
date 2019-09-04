<template>
    <div class="dashboard">
        <v-snackbar v-model="deleteInfo" :timeout="4000" top color="success">
            <span>user successfully deleted!</span>
            <v-btn color="white" flat @click="deleteInfo = false">Close</v-btn>
        </v-snackbar>
        <h1 class="mx-2 my-2 subheading grey--text">Users</h1>
        <v-container class="my-5">
            <v-layout row justify-space-between class="mb-3">
                <v-flex xs12 sm6 md3>
                    <v-text-field
                            label="search a user"
                            v-model="search"
                    ></v-text-field>
                </v-flex>
                <v-flex xs12 sm6 md4>
                    <v-tooltip top>
                        <v-btn small flat color="grey" @click="sortBy('name')" slot="activator">
                            <v-icon small left>account_circle</v-icon>
                            <span class="caption text-lowercase">By Full  name</span>
                        </v-btn>
                        <span>Sort by Full name</span>
                    </v-tooltip>
                    <v-tooltip top>
                        <v-btn small flat color="grey" @click="sortBy('dateRegistry')" slot="activator">
                            <v-icon small left>calendar_today</v-icon>
                            <span class="caption text-lowercase">By Registration Date</span>
                        </v-btn>
                        <span>Sort by Registration Date</span>
                    </v-tooltip>
                </v-flex>
            </v-layout>
            <v-card flat v-for="(user, index) in getUsers" :key="user.id" class="pa-3">
                <v-layout row wrap>
                    <v-flex xs12 md2>
                        <div class="caption grey--text">User Full Name</div>
                        <div>{{ user.name }}</div>
                    </v-flex>
                    <v-flex xs6 sm4 md2>
                        <div class="caption grey--text">Email</div>
                        <div>{{ user.email }}</div>
                    </v-flex>
                    <v-flex xs6 sm4 md3>
                        <div class="caption grey--text">Birth Date</div>
                        <div>{{ user.bday }}</div>
                    </v-flex>
                    <v-flex xs6 sm4 md3>
                        <div class="caption grey--text">Registration Date</div>
                        <div>{{ user.dateRegistry }}</div>
                    </v-flex>
                    <v-flex xs4 sm4 md2>
                        <div class="right">
                            <v-tooltip bottom>
                                <v-btn @click="deleteUser(index, user.id)" depressed fab small color="error white--text" slot="activator">
                                    <v-icon>delete</v-icon>
                                </v-btn>
                                <span>delete</span>
                            </v-tooltip>
                            <router-link :to="{ name: 'user-logs', params: { id: user.id }}">
                                <v-tooltip bottom>
                                        <v-btn depressed fab small color="success white--text" slot="activator">
                                            <v-icon>more</v-icon>
                                        </v-btn>
                                        <span>view logs</span>
                                </v-tooltip>
                            </router-link>
                        </div>
                    </v-flex>
                </v-layout>
                <v-divider></v-divider>
            </v-card>

        </v-container>

    </div>
</template>

<script>
import Pusher from 'pusher-js'
 import axios from 'axios'
    export default {
        data() {
            return {
                users: [],
                search: '',
                deleteInfo: false,
                // url: 'http://192.168.43.137:8082/'
                url: 'http://localhost:8082/'
            }
        },
        computed:{
            getUsers: function() {
                return this.users.filter((user) =>{
                    return user.name.match(this.search)
                })
            }
        },
        methods: {
            deleteUser(index, userId){
                var confirmation = confirm('are you sure to delete this user ! ')
                if (confirmation){
                    this.users.splice(index);
                    this.$http.delete(this.url +  'utilisateurs/' + userId).then(() => {
                        this.deleteInfo = true;
                    });
                    // this.$emit('userDeleted')
                }
            },
            sortBy(prop) {
                this.users.sort((a,b) => a[prop] < b[prop] ? -1 : 1)
            }
        },
        created() {
            this.$http.get(this.url +  'utilisateurs')
                .then(response => (this.users = response.data._embedded.utilisateurs))

           var pusher = new Pusher('84bee67aad46ed497369', {
                cluster: 'eu',
                forceTLS: true
            });

            let iduser = this.$route.params.id
            var channel = pusher.subscribe('my-channel');
            channel.bind('add-user', (data) => {
                  
                axios.get(this.url +  'utilisateurs')
                .then(response => (
            
                    this.users = response.data._embedded.utilisateurs
                ))
      
             });
        }
    }
</script>

<style>
    .project.complete{
        border-left: 4px solid #3cd1c2;
    }
    .project.ongoing{
        border-left: 4px solid #ffaa2c;
    }
    .project.overdue{
        border-left: 4px solid #f83e70;
    }
    .v-chip.complete{
        background: #3cd1c2;
    }
    .v-chip.ongoing{
        background: #ffaa2c;
    }
    .v-chip.overdue{
        background: #f83e70;
    }
</style>
