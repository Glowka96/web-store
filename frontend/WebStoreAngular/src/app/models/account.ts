import { AccountAddress } from './account-address';

export interface Account {
  firstName: string;
  lastName: string;
  email: string;
  address: AccountAddress;
  imageUrl: string;
}
