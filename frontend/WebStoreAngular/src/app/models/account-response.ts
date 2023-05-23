import { AccountAddress } from './account-address';

export interface AccountResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  address: AccountAddress;
  imageUrl: string;
}
