<template>
  <div class="row">
    <div class="col-1"></div>
    <div class="col-10 pt-5 mb-5">
      <div class="container-fluid pe-0 ps-0 me-0 ms-0">
        <div class="align-items-center">
          <div class="row main-col">
            <div class="col d-flex justify-content-start ps-0 ms-0 me-0 pe-0">
              <div class="col mb-1">
                <div class="d-flex flex-row justify-content-end">
                  <button v-if="hasChanged" class="btn btn-red mb-1 me-1" data-bs-toggle="modal" data-bs-target="#confirmationDialog">
                    Save Changes
                  </button>
                </div>
                <div class="modal fade" id="confirmationDialog" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Rental Objects' Status Change</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <div class="modal-body">
                        {{modalMessage}}
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-red" style="margin-right: 2vh;" data-bs-dismiss="modal">No</button>
                        <button type="button" class="btn" @click="saveChanges" data-bs-dismiss="modal">Yes</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <router-link to="/boatOwner/newBoat" class="btn btn-default mb-1 d-flex my-auto">
                <font-awesome-icon class="my-auto pe-2" icon="plus"></font-awesome-icon>
                Create new
              </router-link>
            </div>
          </div>
          <div class="row main-col text-center header rounded mb-3">
            <div class="card" v-for="(rentalObject, index) in this.boats" :key="index" style="width:275px; margin: 0.5%; border-color: #008970">
              <img v-if="rentalObject.photos.length !== 0" :src="imageUrls[index]" style="width:100%" class="img-fluid rounded border-1" alt="">
              <div class="card-body">
                <router-link :to="'/boatOwner/Boat/' + rentalObject.id" style="color: #008970">
                  <h4 class="card-title">{{ rentalObject.name }}</h4>
                  <hr class="ms-1">
                </router-link>
                <div class="row">
                  <h5>
                    <font-awesome-icon class="my-auto" icon="location-dot"></font-awesome-icon>:
                    {{rentalObject.address.city + ", " + rentalObject.address.country}}
                  </h5>
                  <h5>
                    <font-awesome-icon class="my-auto" icon="dollar-sign"></font-awesome-icon>:
                    {{rentalObject.price + "$"}}
                  </h5>
                  <h5>
                    Active: <input class="form-check-input" type="checkbox" :value="!rentalObject.isActive" v-model="rentalObject.isActive" @change="changeStatus(rentalObject)" :disabled="!rentalObject.isDeletable">
                  </h5>
                </div>
              </div>
            </div>
            <nav aria-label="Page navigation">
              <ul class="pagination justify-content-center mt-3">
                <li class="page-item" v-if="totalPages > 1"><button class="page-link" :disabled="currentPage===0" @click="previousPage">Previous</button></li>
                <li class="page-item mt-auto me-1 ms-1" v-if="currentPage > 1 && totalPages > 3">...</li>
                <li class="page-item"><button class="page-link" :disabled="currentPage === 0" @click="numberedPage(1)">{{button1Content}}</button></li>
                <li class="page-item" v-if="totalPages > 1"><button class="page-link" :disabled="currentPage !== 0 && totalPages - currentPage > 1" @click="numberedPage(2)">{{button2Content}}</button></li>
                <li class="page-item" v-if="totalPages > 2"><button class="page-link" :disabled="totalPages - currentPage === 1" @click="numberedPage(3)">{{button3Content}}</button></li>
                <li class="page-item mt-auto me-1 ms-1" v-if="totalPages - currentPage > 1 && totalPages > 3 && button3Content !== totalPages">...</li>
                <li class="page-item" v-if="totalPages > 1"><button class="page-link" :disabled="totalPages - currentPage === 1" @click="nextPage">Next</button></li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
      <div class="col-1"></div>
    </div>
  </div>
</template>

<script>

import axios from "axios";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faAngleDoubleLeft, faAngleDoubleRight, faAngleLeft, faAngleRight, faFrown, faPlus}
  from "@fortawesome/free-solid-svg-icons";
import {useStore} from "vuex";
import {toggleLoading, toggleProcessing} from "@/components/state";

library.add(faAngleRight, faAngleLeft, faFrown, faAngleDoubleRight, faAngleDoubleLeft, faPlus);

export default {
  name: "AllBoatsView",
  components: {
    FontAwesomeIcon
  },
  data() {
    return {
      boats: [],
      totalPages: null,
      currentPage: 0,
      pageSize: 10,
      imageUrls: [],
      changedRentals: []
    }
  },
  mounted() {
    toggleLoading();
    const store = useStore();
    axios.get("/RentalObjects/getBoatsOwner", {headers: {
        Authorization: "Bearer " + store.state.access_token,
      },
      params: {
        page: this.currentPage,
        pageSize: this.pageSize,
        field: "name",
      }},
    ).then(response => {
      this.boats = response.data.content;
      this.currentPage = response.data.currentPage;
      this.totalPages = response.data.pages;
      this.boats.forEach(rentalObject => {
        if(rentalObject.photos.length > 0){
          const index = this.boats.indexOf(rentalObject);
          this.loadImage(rentalObject.photos[0].photo, index);
        }
      });
      toggleLoading();
    })
        .catch(() =>{
          this.$notify({
            title: "Server error",
            text: "Something went wrong. Please try again later...",
            type: "error"
          });
        })
  },
  methods: {
    saveChanges() {
      toggleProcessing();
      let ids = this.changedRentalsIds;
      let lwc = {list: ids};
      axios.put("/RentalObjects/multipleRentalsStatusChange",
          lwc,
          {headers: {
              Authorization: "Bearer " + this.$store.state.access_token,
            }
          })
          .then((response) => {
            let message = "You have successfully changed status for rental objects:";
            response.data.forEach((rentalObject) => {message += " " + rentalObject.name + ","});
            message = message.slice(0, -1);
            message += ".";
            this.$notify({
              title: "Status Change Successful",
              text: message,
              type: "success"
            });
            this.changedRentals = [];
            toggleProcessing();
          })
          .catch(() =>{
            this.$notify({
              title: "Server error",
              text: "Something went wrong. Please try again later...",
              type: "error"
            });
            toggleProcessing();
          })
    },
    changeStatus(rentalObject) {
      let index = this.changedRentals.findIndex(element => element.id === rentalObject.id);
      if (index > -1) {
        this.changedRentals.splice(index, 1);
      } else {
        this.changedRentals.push(rentalObject);
      }
    },
    previousPage() {
      this.currentPage -= 1;
      this.refreshPage();
    },
    nextPage() {
      this.currentPage += 1;
      this.refreshPage();
    },
    numberedPage(buttonNumber) {
      if (buttonNumber === 1) {
        this.currentPage = this.button1Content - 1;
      } else if (buttonNumber === 2) {
        this.currentPage = this.button2Content - 1;
      } else if (buttonNumber === 3) {
        this.currentPage = this.button3Content - 1;
      }
      this.refreshPage();
    },
    refreshPage() {
      axios.get("/RentalObjects/getBoatsOwner", {headers: {
          Authorization: "Bearer " + this.$store.getters.access_token,
        },
        params: {
          page: this.currentPage,
          pageSize: this.pageSize,
          field: "name"
        }
      })
          .then(response => {
            this.boats = response.data.content;
            this.currentPage = response.data.currentPage;
            this.totalPages = response.data.pages;
            this.boats.forEach(rentalObject => {
              if (rentalObject.photos.length > 0) {
                const index = this.boats.indexOf(rentalObject);
                this.loadImage(rentalObject.photos[0].photo, index);
              }
            });
            toggleProcessing();
          })
          .catch(() =>{
            this.$notify({
              title: "Server error",
              text: "Server is currently off. Please try again later...",
              type: "error"
            });
            toggleProcessing();
          });
    },
    loadImage(name, index) {
      axios.get("/Photos/", {headers: {
          Authorization: "Bearer " + this.$store.getters.access_token,
        },
        params: {
          path: name,
        },
        responseType: "blob"
      })
          .then(response => {
            this.imageUrls[index] = URL.createObjectURL(response.data);
          })
          .catch((error) =>{
            console.log(error);
          });
    },
    rulesOfConduct(rulesOfConduct) {
      let pairsOfRules = [];
      let positiveRules = this.positiveRules(rulesOfConduct);
      let negativeRules = this.negativeRules(rulesOfConduct);
      let size = Math.max(positiveRules.length, negativeRules.length);
      for (let i = 0; i < size; i++) {
        pairsOfRules.push({
          "Do": i+1<=positiveRules.length?positiveRules[i]:"",
          "DoNot": i+1<=negativeRules.length?negativeRules[i]:""
        });
      }
      return pairsOfRules;
    },
    positiveRules(rulesOfConduct) {
      let positiveRules = [];
      rulesOfConduct.forEach(rule => {
        if (rule.type === "Do") {
          positiveRules.push(rule.rule);
        }
      });
      return positiveRules;
    },
    negativeRules(rulesOfConduct) {
      let negativeRules = [];
      rulesOfConduct.forEach(rule => {
        if (rule.type === "DoNot") {
          negativeRules.push(rule.rule);
        }
      });
      return negativeRules;
    },
    equipment(equipment) {
      let result = "";
      equipment.forEach(element => {
        result += " " + element.name + ",";
      })
      result = result.slice(0, -1);
      return result;
    }
  },
  computed: {
    changedRentalsIds() {
      let ids = [];
      this.changedRentals.forEach(rentalObject => {ids.push(rentalObject.id)});
      return ids;
    },
    hasChanged() {
      return this.changedRentals.length > 0;
    },
    button1Content() {
      return this.currentPage === 0 ? this.currentPage+1: this.totalPages - this.currentPage === 1 ? this.currentPage - 1 : this.currentPage;
    },
    button2Content() {
      return this.currentPage === 0 ? this.currentPage+2: this.totalPages - this.currentPage === 1 ? this.currentPage : this.currentPage+1;
    },
    button3Content() {
      return this.currentPage === 0 ? this.currentPage+3: this.totalPages - this.currentPage === 1 ? this.currentPage + 1 : this.currentPage+2;
    },
    modalMessage() {
      let numberOfDeletedRentals = this.numberOfDeletedRentals;
      let numberOfActivatedRentals = this.numberOfActivatedRentals;
      let deleteMessage = "";
      let activateMessage = "";
      if (numberOfDeletedRentals > 1) {
        deleteMessage = "Do you want to delete boats:";
      } else if (numberOfDeletedRentals === 1) {
        deleteMessage = "Do you want to delete boat:";
      }
      if (numberOfActivatedRentals > 1) {
        activateMessage = "Do you want to activate boats:";
      } else if (numberOfActivatedRentals === 1) {
        activateMessage = "Do you want to activate boat:";
      }
      this.changedRentals.forEach(rentalObject => {
        if (!rentalObject.isActive) {
          deleteMessage += " " + rentalObject.name + ",\n";
        } else {
          activateMessage += " " + rentalObject.name + ",\n";
        }
      });
      if (numberOfDeletedRentals > 0) {
        deleteMessage = deleteMessage.slice(0, -2);
        deleteMessage += "?";
      }
      if (numberOfActivatedRentals > 0) {
        activateMessage = activateMessage.slice(0, -2);
        activateMessage += "?";
      }
      return deleteMessage + "\n" + activateMessage;
    },
    numberOfDeletedRentals() {
      let counter = 0;
      this.changedRentals.forEach(rentalObject => {
        if (!rentalObject.isActive) {
          counter += 1;
        }
      });
      return counter;
    },
    numberOfActivatedRentals() {
      let counter = 0;
      this.changedRentals.forEach(rentalObject => {
        if (rentalObject.isActive) {
          counter += 1;
        }
      });
      return counter;
    }
  }
}
</script>

<style scoped>

div.card {
  border-radius: 20px;
  box-shadow:  0 0.3rem 1rem rgb(0 0 0 / 30%);
}
</style>