<template>
  <div class="row">
    <div class="col-2"></div>
    <div class="col-8 pt-5 mb-5">
      <div class="container px-4 py-3 rounded form" spellcheck="false" >
        <div class="container-fluid">
          <div class="form-text">
            <font-awesome-icon class="d-inline-block" icon="user-slash" style="color:#008970; height: 28px"></font-awesome-icon>
            <h3 class="d-inline-block" style="color: #008970">Account Deletion</h3>
          </div>
          <div class="row main justify-content-center">
            <div class="row main">
              <div class="col-2"></div>
              <div class="col-8">

                <div class="form-group">
                  <textarea id="enteredRequest" v-model="enteredRequest" class="form-control" placeholder="Please enter a request..." maxlength="255"></textarea>
                  <p style="color: red; font-size: small" v-if="enteredRequest.length === 255">Oops! The maximum number of characters is 255.</p>
                </div>

                <button type="button" class="btn btn-default" :disabled='isDisabled' data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                  Submit
                </button>

                <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="Label" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="Label" style="color: #008970">Password confirmation</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <div class="modal-body">
                        <input type="password" id="inputPassword" v-model="passwordConfirmation" placeholder="Please enter password...">
                        <p style="color: red; font-size: small" v-if="!isFilled">You must enter a password.</p>
                      </div>
                      <div class="modal-footer">
                        <button class="btn btn-default" id="button" @click.prevent="submitRequest" :disabled='!isFilled'>
                          Submit
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUserSlash } from "@fortawesome/free-solid-svg-icons";
import axios from "axios/index";
import store from "@/store";

library.add(faUserSlash);
export default {
  name: "AccountDeletionForm",
  components: {
    FontAwesomeIcon
  },
  data() {
    return {
      passwordConfirmation: '',
      enteredRequest: ''
    }
  },
  methods: {
    submitRequest () {
      axios.post("/Requests/delAcc", {
        email: this.email,
        password: this.passwordConfirmation,
        enteredRequest: this.enteredRequest,
        requestType: 'AccountDeletion'
      }, {
        headers: {
          Authorization: "Bearer " + this.accessToken
        }
      })
      .then(() => {
        this.$notify( {
          title: "Account deletion Notification",
          text: "Request to delete account successfully sent.",
          position: "bottom right",
          type: "success"
        });
      })
      .catch ((error) =>{
        if(error.response.status===404){
          this.$notify({
            title: "User not found",
            text: "User with the specified e-mail was not found!",
            type: "warn"
          })
        } else if(error.response.status===401){
          this.$notify({
            title: "Authorization Failed",
            text: "Password is not valid!",
            type: "error"
          })
        } else if(error.response.status===400) {
          this.$notify({
            title: "Invalid Request Status",
            text: "Request status does not have the right form!",
            type: "warn"
          })
        } else if(error.response.status===500){
          this.$notify({
            title: "Internal Server Error",
            text:"Something went wrong on the server! Please try again later.",
            type: "error"
          })
        }
      })
    }
  },
  computed: {
    isDisabled() {
      return this.enteredRequest.length < 30;
    },
    isFilled() {
      return this.passwordConfirmation !== '';
    },
    accessToken(){
      return store.state.access_token;
    },
    email(){
      return store.state.email;
    }
  }
}
</script>

<style scoped>
form {
  background-color: #fcfcfc;
  outline: solid 2px #008970;
  margin-top: 40px;
  color: #008970;
}

textarea {
  height: 300px;
  min-height: 100px;
}
</style>