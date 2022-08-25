<template>
  <div v-if="rentalObject" class="container-fluid mt-2 pe-0">
    <div class="row align-items-start">
      <div class="col-md-6 p-3">
        <ImageSlider :photos="this.rentalObject.photos"/>

      </div>

      <div class="col-md-6 p-3 d-flex flex-grow-1 justify-content-center align-items-center rentalBasicInfo">
        <div style="width: 100%;">
          <div class="row">
            <div class="align-items-center text-start" style="display: flex;">
              <hr class="ms-1">
              <div>
                <h1 class="display-3 card-title cut-text" :title="this.rentalObject.name">{{ this.rentalObject.name }}
                  <router-link v-if="isVacationRentalOwner" :to="'/vacationRentalOwner/updateVacationRental/' + this.$route.params.id" class="btn btn-default" style="height: fit-content;" :class="!this.rentalObject.isDeletable ? 'disabled':''">
                    <font-awesome-icon class="h4" icon="pencil"></font-awesome-icon>
                  </router-link>
                  <router-link v-else :to="'/boatOwner/updateBoat/' + this.$route.params.id" class="btn btn-default" style="height: fit-content;" :class="!this.rentalObject.isDeletable ? 'disabled':''">
                    <font-awesome-icon class="h4" icon="pencil"></font-awesome-icon>
                  </router-link>
                </h1>
              </div>
              <hr class="ms-1">
            </div>
          </div>

          <div class="row my-0 mb-2">
            <div class="col-12 d-flex justify-content-center align-items-center">
              <star-rating v-model:rating="this.rentalObject.grade" text-class="h1 my-0 font-weight-normal" :read-only="true" :round-start-rating="false" :glow="5" glow-color="#ffd055"></star-rating>
            </div>
          </div>

          <div v-if="isVacationRental" class="row main mb-3">
            <div class="col main d-flex justify-content-center" title="Capacity">
              <p class="display-5">
                <font-awesome-icon class="me-1" icon="user"></font-awesome-icon><strong>{{ this.rentalObject.capacity }}</strong>
              </p>
            </div>
            <div class="col main d-flex justify-content-center" title="Number of rooms">
              <p class="display-5">
                <font-awesome-icon class="me-1" icon="door-open"></font-awesome-icon><strong>{{ getNumberOfRooms }}</strong>
              </p>
            </div>
            <div class="col main d-flex justify-content-center" title="">
              <p class="display-5" :title="this.bedsPerRoom">
                <font-awesome-icon class="me-1" icon="bed"></font-awesome-icon><strong>{{ getNumberOfBeds }}</strong>
              </p>
            </div>
          </div>

          <div v-if="isBoat" class="row main mb-3">
            <div class="col main d-flex justify-content-center" title="Type">
              <p class="display-6">
                <font-awesome-icon class="me-1" icon="ship"></font-awesome-icon><strong>{{ this.rentalObject.type }}</strong>
              </p>
            </div>
            <div class="col main d-flex justify-content-center" title="Maximum speed">
              <p class="display-6">
                <font-awesome-icon class="me-1" icon="gauge"></font-awesome-icon><strong>{{ this.rentalObject.maxSpeed + 'km/h'}}</strong>
              </p>
            </div>
            <div class="col main d-flex justify-content-center" title="Length">
              <p class="display-6">
                <font-awesome-icon class="me-1" icon="ruler"></font-awesome-icon><strong>{{ this.rentalObject.length + 'm'}}</strong>
              </p>
            </div>
          </div>

          <div v-if="isBoat" class="row main mb-3">
            <div class="col main d-flex justify-content-center" title="Engine number">
              <p class="h4">
                Engine number: {{ this.rentalObject.engineNumber }}
              </p>
            </div>
            <div class="col main d-flex justify-content-center" title="Engine power">
              <p class="h4">
                Engine power: {{ this.rentalObject.enginePower }}
              </p>
            </div>
          </div>

          <div v-if="isVacationRentalOwner" class="row">
            <div class="d-flex justify-content-center">
              <router-link :to="'/vacationRentalOwner/specialOfferVacationRental/' + this.$route.params.id" class="btn mt-3 me-1"><font-awesome-icon style="margin-right: 10px" icon="tag"></font-awesome-icon>Special Offer</router-link>
              <router-link :to="'/vacationRentalOwner/reservation/' + this.$route.params.id" class="btn mt-3 me-1"><font-awesome-icon style="margin-right: 10px" icon="bookmark"></font-awesome-icon>Book</router-link>
            </div>
          </div>

          <div v-if="isBoatOwner" class="row">
            <div class="d-flex justify-content-center">
              <router-link :to="'/boatOwner/specialOfferBoat/' + this.$route.params.id" class="btn mt-3 me-1"><font-awesome-icon style="margin-right: 10px" icon="tag"></font-awesome-icon>Special Offer</router-link>
              <router-link :to="'/boatOwner/reservation/' + this.$route.params.id" class="btn mt-3 me-1"><font-awesome-icon style="margin-right: 10px" icon="bookmark"></font-awesome-icon>Book</router-link>
            </div>
          </div>

        </div>
      </div>
    </div>

    <div class="row align-items-start mb-5">
      <div class="col-md-6 ps-3">
        <div class="row">
          <div class="" style="">
            <RentalDescription :description="this.rentalObject.description"/>

            <RentalRules :conduct-rules="this.rentalObject.conductRules"/>

            <div class="lineTitle">
              <p class="h3"><strong>Availability period</strong></p>
              <hr class="ms-1">
            </div>
            <div class="row justify-content-center">
              <date-picker :key="rentalObject.id" v-model="range" is-range :first-day-of-week=2 @drag="rangeChanged=true"></date-picker>
            </div>
            <div v-if="rangeChanged" class="row mt-1 justify-content-center">
              <button class="btn default" style="max-width: fit-content" @click="updatePeriod">Save changes</button>
            </div>

          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div v-if="isVacationRental" class="row main mb-3">
          <div class="lineTitle">
            <p class="h3"><strong>Included services</strong></p>
            <hr class="ms-1">
          </div>
          <div class="tags row ps-3 pe-1 mb-1">
          <RentalTags :additional-services="this.rentalObject.additionalServices" />
          </div>
        </div>

        <div v-if="isBoat" class="row main mb-3">
          <div class="lineTitle">
            <p class="h3"><strong>Navigation equipment</strong></p>
            <hr class="ms-1">
          </div>
          <div class="tags row ps-3 pe-1 mb-1">
            <div v-for="(tag, i) in this.rentalObject.navigationEquipment" :key="i" class="tagDiv chip chip-outline btn-outline-primary me-2 px-3 my-1" data-ripple-color="dark">
              {{tag.name}}
            </div>
          </div>
        </div>

        <div v-if="isBoat" class="row main mb-5">
          <div class="lineTitle">
            <p class="h3"><strong>Fishing equipment</strong></p>
            <hr class="ms-1">
          </div>
          <div class="tags row ps-3 pe-1 mb-1">
            <div v-for="(tag, i) in this.rentalObject.fishingEquipment" :key="i" class="tagDiv chip chip-outline btn-outline-primary me-2 px-3 my-1" data-ripple-color="dark">
              {{tag.name}}
            </div>
          </div>
        </div>

        <div class="row mb-5" >
          <div class="col-12 p-0 px-2 m-0">
            <RentalAddress :address="this.rentalObject.address"/>
          </div>
        </div>
      </div>
    </div>

      <div class="col-md-6" >

    </div>
  </div>

</template>

<script>
import ImageSlider from "@/components/UnregisteredUser/components/Rental/ImageSlider";
import RentalDescription from "@/components/UnregisteredUser/components/Rental/RentalDescription";
import RentalTags from "@/components/UnregisteredUser/components/Rental/RentalTags";
import RentalAddress from "@/components/UnregisteredUser/components/Rental/RentalAddress";
import RentalRules from "@/components/UnregisteredUser/components/Rental/RentalRules";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {library} from "@fortawesome/fontawesome-svg-core";
import { faBed, faCircleCheck, faCircleXmark, faClock, faDoorOpen, faLocationDot, faUser, faUserTie, faPencil, faTrash, faTag, faBookmark, faShip, faRuler, faGauge } from "@fortawesome/free-solid-svg-icons";
import axios from "axios/index";
import {useStore} from "vuex";
import StarRating from 'vue-star-rating';
import {DatePicker} from "v-calendar";
import store from "@/store";
import {toggleProcessing} from "@/components/state";


library.add(faUser, faDoorOpen, faBed, faClock, faLocationDot, faCircleCheck, faCircleXmark, faUserTie, faPencil, faTrash, faTag, faBookmark, faShip, faRuler, faGauge);

export default {
  name: "VacationRentalProfile",
  components: {
    RentalRules, RentalAddress, RentalTags, RentalDescription, ImageSlider, FontAwesomeIcon, StarRating, DatePicker},
  data() {
    return {
      rentalObject: null,
      currentPage: 0,
      totalPages: null,
      pageSize: 6,
      images: [],
      bedsPerRoom: null,
      range: {
        start: null,
        end: null
      },
      rangeChanged: false
    }
  },
  computed: {
    ownerOrInstructor() {
      if(this.rentalObject.rentalObjectType === "Adventure") return "Instructor";
      return "Owner";
    },
    isVacationRentalOwner() {
      return this.$store.getters.user === "vacationRentalOwner";
    },
    isBoatOwner() {
      return this.$store.getters.user === "boatOwner";
    },
    getNumberOfRooms() {
      return this.rentalObject.rooms.length;
    },
    getNumberOfBeds() {
      let beds = 0;
      this.rentalObject.rooms.forEach(room => { beds += room.beds; });
      return beds;
    },
    doesRentalHavePhotos() {
      return this.rentalObject.photos.length > 0;
    },
    isVacationRental() {
      return this.rentalObject.rentalObjectType === "VacationRental";
    },
    isBoat() {
      return this.rentalObject.rentalObjectType === "Boat";
    },
    getAccessToken() {
      return store.state.access_token;
    },
  },
  mounted() {
    const store = useStore();
    if(this.$route.params.type === "Boat") {
      axios.get("/RentalObjects/getBoat", {
        headers: {
          Authorization: "Bearer " + store.state.access_token
        },
        params: {
          id: this.$route.params.id,
          page: this.currentPage,
          pageSize: this.pageSize
        }
      })
          .then((response) => {
            this.rentalObject = response.data;
            this.currentPage = this.rentalObject.reviews.currentPage;
            this.totalPages = this.rentalObject.reviews.pages;
            this.getReviewPics();
          });
    }
    else {
      axios.get("/RentalObjects/getVacationRental", {
        headers: {
          Authorization: "Bearer " + store.state.access_token
        },
        params: {
          id: this.$route.params.id,
          page: this.currentPage,
          pageSize: this.pageSize
        }
      })
          .then((response) => {
            this.rentalObject = response.data;
            this.currentPage = this.rentalObject.reviews.currentPage;
            this.totalPages = this.rentalObject.reviews.pages;
            this.getReviewPics();
            this.numOfBedsPerRoom();
          });
    }
  },
  methods: {
    numOfBedsPerRoom() {
      let result = "Beds per room: ";
      for(let i=0; i<this.rentalObject.rooms.length; i++) {
        result += this.rentalObject.rooms[i].beds;
        if(i === this.rentalObject.rooms.length - 1)
          break
        result += " + ";
      }
      this.bedsPerRoom = result;
    },
    updatePeriod() {
      toggleProcessing();
      const start = this.range.start.toISOString().slice(0, 19);
      const end = this.range.end.toISOString().slice(0, 19);
      axios.put("/RentalObjects/updateAvailabilityPeriod", null, {
        headers: {
          Authorization: "Bearer " + this.$store.getters.access_token
        },
        params: {
          id: this.$route.params.id,
          start: start,
          end: end
        }
      })
          .then(response => {
            this.range.start = response.data.initDate;
            this.range.end = response.data.termDate;
            toggleProcessing();
            this.$notify( {
              title: "Successful update",
              text: "You have successfully set availability period.",
              position: "bottom right",
              type: "success"
            });
          })
          .catch((error) => {
            toggleProcessing();
            if (!error.response) {
              this.$notify({
                title: "Server error",
                text: "Server is currently off. Please try again later...",
                type: "error"
              });
            } else if (error.response.status === 500) {
              this.$notify({
                title: "Internal Server Error",
                text: "Something went wrong on the server! Please try again later...",
                position: "bottom right",
                type: "error"
              })
            }
          });
    }
  },
}
</script>

<style scoped>
.cut-text {
  width: 100%;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.page-link {
  background-color: white;
  border: 2px solid black;
}
div.rentalObjectName {
  display: flex;
}
div.lineTitle {
  display: flex;
}

hr {
  flex-grow: 1;
}

div.rentalBasicInfo {
  height: 60vh;
}

.btn-default {
  cursor: pointer;
  user-select: none;
  color: black;
  text-decoration: none;
  background-color: white;
  border: none;
}

.btn-default:hover, .btn-default:active, .btn-default:focus {
  border: 1px solid white;
}

.tagDiv {
  width: fit-content;
  border-radius: 30px;
  border: 1px solid #7a7a7a;
  cursor: pointer;
  font-weight: 400;
  color: #7a7a7a;
}

.tagDiv:hover {
  background-color: #008970;
  color: white;
  border: #fcfcfc;
}
</style>