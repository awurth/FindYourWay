
export default class Geo {
  constructor () {
    this.R = 6371e3
  }

  getKmDistance (lat1, lng1, lat2, lng2) {
    let radianLat1 = this.getRadians(lat1)
    let radianLat2 = this.getRadians(lat2)
    let diffRadianLat = this.getRadians(lat2 - lat1)
    let diffRadianLng = this.getRadians(lng2 - lng1)

    let a = Math.sin(diffRadianLat / 2) * Math.sin(diffRadianLat / 2) +
      Math.cos(radianLat1) * Math.cos(radianLat2) *
      Math.sin(diffRadianLng / 2) * Math.sin(diffRadianLng / 2)

    let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return (this.R * c) / 1000
  }

  getRadians (degrees) {
    return degrees * Math.PI / 180
  }
}
