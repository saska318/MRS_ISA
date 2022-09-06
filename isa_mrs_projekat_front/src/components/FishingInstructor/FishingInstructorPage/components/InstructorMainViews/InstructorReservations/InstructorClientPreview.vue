<template>
  <div class="modal fade" :id="'clientProfile-' + this.reservationId" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">
            <div class="row">
              <p class="display-6">
                <font-awesome-icon class="me-3" icon="user"></font-awesome-icon>
                <strong>{{client.name + " " + client.surname}}</strong>
              </p>
            </div>
          </h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="row justify-content-center">
            <div class="row">
              <div class="col me-lg-3 me-md-2">
                <div class="row text-center">
                  <img v-if="client.photo && this.profilePic" :src="this.profilePic" class="img-fluid p-0" alt="" style="border-radius: 100%;">
                  <font-awesome-icon v-else icon="user" class="img-fluid rounded p-0" style="background-color: #B0B8B4FF; color: white"></font-awesome-icon>
                </div>
              </div>
              <div class="col">
                <div class="row text-center">
                  <div class="container-fluid rounded" style="border: 1px solid #008970; color: #008970" >
                    <div class="row pb-5">
                      <div class="col text-start">
                        <h5>Activity:
                          <input v-if="client.isActive" class="form-check-input" type="checkbox" checked disabled>
                          <input v-else class="form-check-input" type="checkbox" disabled></h5>
                        <h5>
                          <font-awesome-icon icon="at"></font-awesome-icon>:
                          <span style="color: black"> {{client.email}}</span>
                        </h5>
                        <h5>
                          <font-awesome-icon icon="phone"></font-awesome-icon>:
                          <span style="color: black"> {{client.phone}}</span>
                        </h5>
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
import axios from "axios";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faAt, faPhone} from "@fortawesome/free-solid-svg-icons";

library.add(faAt, faPhone);

export default {
  name: "InstructorClientPreview",
  props: ["client", "reservationId"],
  components: {FontAwesomeIcon},
  data() {
    return {
      profilePic: null,
    }
  },
  mounted() {
    if(!this.client.photo) {
      this.profilePic = "../../../../../Images/noProfilePic.png";
      return;
    }
    axios.get("/Photos/", {
      headers: {
        Authorization: "Bearer " + this.$store.getters.access_token,
      },
      params: {
        path: this.client.photo,
      },
      responseType: "blob"
    })
        .then(response => {
          this.profilePic = URL.createObjectURL(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
  }
}
</script>

<style scoped>
.rounded-circle {
  background-color:  rgba(176, 184, 180);
  color: white;
  padding: 10px;
}

.profile-link {
  color: #008970;
}

.modal-title {
  color: #008970;
}
</style>