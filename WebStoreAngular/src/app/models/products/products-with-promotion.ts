export interface ProductWithPromotion {
  id: number;
  name: string;
  imageUrl: string;
  quantity: number;
  productType: string;
  price: number;
  promotionPrice: number;
  lowestPrice: number;
  endDate: Date;
}
