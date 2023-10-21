public class Order{
    private OrderState state;
    private ShippingInfo shippingInfo;

    public void changeShippingInfo(ShippingInfo newShippingInfo){
        if(!isShippingChangeable()){
            throw new IllegalStateException("현재 배송지 변경이 불가합니다. 주문 상태 : " + state);
        }

        this.shippingInfo = newShippingInfo;
    }

    private boolean isShippingChangeable(){
        return state  == OrderState.PAYMENT_WAITING || state == OrderState.PREPARING;
    }

}
