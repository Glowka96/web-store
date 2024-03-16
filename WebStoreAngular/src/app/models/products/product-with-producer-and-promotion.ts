export interface ProductWithProducerAndPromotion {
  id: number;
  name: string;
  imageUrl: string;
  quantity: number;
  productTypeName: string;
  price: number;
  promotionPrice: number;
  lowestPrice: number;
  endDate: Date;
  description: string;
  producerName: string;
}
