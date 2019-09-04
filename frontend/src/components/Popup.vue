<template>
    <v-dialog max-width="600px" v-model="dialog">
        <v-btn flat slot="activator" class="success">Add New User</v-btn>
        <v-card>
            <v-card-title>
                <h2>Add a New User</h2>
            </v-card-title>
            <v-card-text>
                <v-form class="px-3" ref="form">
                    <v-text-field v-model="fullName" label="Full Name" prepend-icon="folder" :rules="inputRules"></v-text-field>
                    <v-text-field v-model="email"  label="Email" prepend-icon="email"></v-text-field>

                    <v-menu v-model="menu" :close-on-content-click="false">
                        <v-text-field slot="activator" :rules="inputRules"
                                      :value="formattedDate" clearable label="Birth Date" prepend-icon="date_range">
                        </v-text-field>
                        <v-date-picker v-model="birthDay" @change="menu = false"></v-date-picker>
                    </v-menu>

                    <v-spacer></v-spacer>

                    <v-btn flat @click="submit" class="success mx-0 mt-3" :loading="loading">Add User</v-btn>
                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
    import format from 'date-fns/format'
    export default {
        data() {
            return {
                fullName: '',
                email: '',
                birthDay: '',
                menu: false,
                inputRules: [
                    v => !!v || 'This field is required',
                    v => v.length >= 3 || 'Minimum length is 3 characters'
                ],
                emailRules: [
                    v => /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) || 'E-mail must be valid'
                ],
                loading: false,
                dialog: false
            }
        },
        methods: {
            submit() {
                if (this.$refs.form.validate()) {
                    this.loading = true
                    this.$http.post('http://localhost:8082/executeWrite', {
                        name: this.fullName,
                        date: this.birthDay,
                        email: this.email
                    }).then((response) => {
                        this.loading = false
                        this.dialog = false
                        this.$emit('projectAdded')
                    }).catch((e) => {
                        this.loading = false
                        this.dialog = false
                        this.$emit('projectAdded')
                    })
                }
            }
        },
        computed: {
            formattedDate () {
                console.log(this.birthDay)
                return this.birthDay ? format(this.birthDay, 'YYYY MMM Do ') : ''
            }
        }
    }
</script>
