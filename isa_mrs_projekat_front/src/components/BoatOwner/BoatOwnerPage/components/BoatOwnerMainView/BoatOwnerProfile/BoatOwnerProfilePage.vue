<template>
  <div class="page-holder ps-1">
    <div class="container-fluid px-lg-4 px-xl-5 p-0 m-0 pt-5 contentDiv">
      <section class="p-0 m-0">
        <div v-if="this.boatOwner" class="row">
          <div class="col-lg-4" style="height: fit-content; display:block; min-width: 18rem;">
            <div class="card card-profile mb-4" style="margin-left: 36%">
              <div class="card-body text-left">
                <div class="row">
                  <div class="col-4" style="margin-left: 4%">
                  <img v-if="this.profilePic" class="card-profile-img" :src="this.profilePic" alt=""/>
                  <img v-else class="card-profile-img" src="../../../../../Images/boatOwnerNoProfilePic.png" alt=""/>
                    <div class="d-flex justify-content-left">
                      <p class="h2" style="color: #008970; font-family: Cambria">{{ fullName }}</p>
                      <router-link :to="'/vacationRentalOwner/profile/' + this.boatOwner.id" class="btn btn-default ms-2" style="height: fit-content;">
                        <font-awesome-icon class="h5" icon="pencil" style="color: #008970"></font-awesome-icon>
                      </router-link>
                    </div>
                    <hr>
                    <div class="row text-start">
                      <div class="row">
                        <h5 style="font-family: Cambria; font-style: oblique">
                          <font-awesome-icon class="h4" icon="envelope" style="color: #008970"></font-awesome-icon>
                          : {{ this.boatOwner.email }}
                        </h5>
                      </div>
                      <div class="row">
                        <h5 style="font-family: Cambria">
                          <font-awesome-icon class="h4" icon="phone" style="color: #008970"></font-awesome-icon>
                          : {{ this.boatOwner.phone }}
                        </h5>
                      </div>
                    </div>
                  </div>
                  <div class="col-2"></div>
                  <div class="col-5" style="margin-top: 50px">
                    <RentalAddress v-if="this.boatOwner.address" :address="this.boatOwner.address"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import RentalAddress from "@/components/UnregisteredUser/components/Rental/RentalAddress";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faPencil, faEnvelope, faPhone} from "@fortawesome/free-solid-svg-icons";


library.add(faPencil, faEnvelope, faPhone);

export default {
  name: "BoatOwnerProfilePage",
  components: {FontAwesomeIcon, RentalAddress},
  data() {
    return {
      boatOwner: null,
      subscribersBtnClicked: false,
      reviewsBtnClicked: true,
      profilePic: null,
    }
  },
  mounted() {
    axios.get("/RentalOwners/getBoatOwner", {
      headers: {
        Authorization: "Bearer " + this.$store.getters.access_token
      },
      params: {
        id: this.$route.params.id,
      }
    })
        .then((response) => {
          this.boatOwner = response.data;
          this.getProfilePic();
        })
        .catch((error) => {
          console.log(error);
        });
  },
  computed: {
    fullName() {
      return this.boatOwner.name + " " + this.boatOwner.surname;
    },
  },
  methods: {
    getProfilePic() {
      if(!this.boatOwner.photo) { this.profilePic = null; return; }
      axios.get("/Photos/", {
        headers: {
          Authorization: "Bearer " + this.$store.getters.access_token,
        },
        params: {
          path: this.boatOwner.photo,
        },
        responseType: "blob"
      })
          .then(response => {
            this.profilePic = URL.createObjectURL(response.data);
          })
          .catch((error) => {
            console.log(error);
          });
    },
    resetTabButtons() {
      this.subscribersBtnClicked = false;
      this.reviewsBtnClicked = false;
    },
    showSubscribers() {
      this.resetTabButtons();
      this.subscribersBtnClicked = true;
    },
    showReviews() {
      this.resetTabButtons();
      this.reviewsBtnClicked = true;
    },
  },
}
</script>

<style scoped>
.btn-default {
  cursor: pointer;
  user-select: none;
  color: #3f5b25;
  text-decoration: none;
  background-color: white;
  border: none;
}

.btn-default:hover, .btn-default:active, .btn-default:focus {
  border: 1px solid white;
}

div.tabs button {
  border: none;
  background-color: inherit;
}

.card {
  position: center;
  display: flex;
  height: fit-content;
  width: 1000px;
  min-width: 0;
  word-wrap: break-word;
  background-color: #fff;
  background-clip: border-box;
  border: none;
  box-shadow: 0 0.5rem 1rem rgb(0 0 0 / 15%);
  border-radius: 1rem;
}

h1, .h1, h2, .h2, h3, .h3, h4, .h4, h5, .h5, h6, .h6 {
  line-height: 1.2;
}

.page-holder {
  display: flex;
  overflow-x: hidden;
  width: 100%;
  min-height: calc(100vh - 72px);
  flex-wrap: wrap;
}

a {
  color: #4650dd!important;
  text-decoration: underline!important;
  cursor: pointer;
}

.card-profile-img {
  margin-bottom: 1rem;
  border: none;
  box-shadow: 0 0.5rem 1rem rgb(0 0 0 / 15%);
  z-index: 2;
  width: 18rem;
  height: 18rem;
  min-width: 12rem;
  min-height: 12rem;
  border-radius: 2%;
  object-fit: cover;
  object-position: center;
}

img, svg {
  vertical-align: middle;
}

.contentDiv {
  padding-top: 4rem;
}

.card-profile .card-header {
  height: 15rem;
  background-position: center center;
  background-size: cover;
}
</style>